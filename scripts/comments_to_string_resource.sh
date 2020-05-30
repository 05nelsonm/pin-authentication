#!/bin/bash

# This script gets us most of the way for reading in comments
# and methods to prepare for use as a string resource. Tweaks
# to line lengths are still needed.

# Get current directory where the script is located
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"

FILE=$1

if [ "$FILE" == "" ]; then
  FILE=$DIR/../pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt
fi

if [ ! -f $FILE ]; then
  echo "File does not exist"
  exit 1
fi

OUTPUT_FILE="$DIR/output.txt"

if [ -f $OUTPUT_FILE ]; then
  mv $OUTPUT_FILE "$DIR/output.bak.txt"
  echo ""
  echo "File:"
  echo "$OUTPUT_FILE"
  echo "already exists and has been renamed to output.bak.txt"
  echo ""
fi

touch $OUTPUT_FILE

# Read in comments
READING_COMMENT=false
GET_METHOD=false

is_line_begining_of_comment() {
  if [ "$1" == "/**" ]; then
    READING_COMMENT=true
  fi
}

is_line_end_of_comment() {
  if [ "$1" == "* */" ]; then
    READING_COMMENT=false
    GET_METHOD=true
    echo "\n${1}" >> $OUTPUT_FILE
    return 0
  else
    return 1
  fi
}

is_line_sample() {
  if [ ${#1} -gt 8 ]; then
    local FIRST_NINE=
    FIRST_NINE=$(echo "$1" | cut -c 1-9)

    if [ "$FIRST_NINE" == "* @sample" ];then
      return 0
    else
      return 1
    fi

  fi

  return 1
}

while read -r LINE; do

  if ! $READING_COMMENT; then
    is_line_begining_of_comment "$LINE"
  fi

  if $READING_COMMENT; then

    if ! is_line_end_of_comment "$LINE"; then

      if ! is_line_sample "$LINE"; then

        # Escape our apostrophes
        LINE=$(echo "$LINE" | sed "s/'/\\\'/g")

        # Wrap <> types in <![CDATA[]]>
        if echo "$LINE" | grep -q "<" && echo "$LINE" | grep -q ">"; then
          INSIDE=$(echo "$LINE" | sed 's/.*<\(.*\)>.*/\1/')
          LINE=$(echo "$LINE" | sed "s/<.*>/<![CDATA[<$INSIDE>]]>/g")
        fi

        echo "\n$LINE" >> $OUTPUT_FILE

      fi

    fi

  elif $GET_METHOD; then

    if echo "$LINE" | grep -q "fun "; then
      LINE=$(echo "$LINE" | cut -d ')' -f 1)

      # Wrap <> types in <![CDATA[]]>
      if echo "$LINE" | grep -q "<" && echo "$LINE" | grep -q ">"; then
        INSIDE=$(echo "$LINE" | sed 's/.*<\(.*\)>.*/\1/')
        LINE=$(echo "$LINE" | sed "s/<.*>/<![CDATA[<$INSIDE>]]>/g")
      fi

      echo "\n$LINE)\n\n" >> $OUTPUT_FILE
      echo "" >> $OUTPUT_FILE
    fi
    GET_METHOD=false

  fi

done < $FILE
