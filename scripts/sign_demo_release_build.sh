#! /bin/bash

if [ "$ANDROID_SDK" == "" ]; then
  echo "ANDROID_SDK environment variable not set"
  exit 1
fi

# Get current directory where the script is located
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"

# Check for pkcs11 config file
PKCS11_CFG="$DIR/.pkcs11_java.cfg"
if [ ! -f $PKCS11_CFG ]; then
  echo "File not found:"
  echo "$PKCS11_CFG"
  exit 1
fi

# Get Build Tools Version from demo's build.gradle
FILE="$DIR/../demo/build.gradle"

if [ ! -f $FILE ]; then
  echo "File not found:"
  echo "$FILE"
  exit 1
fi

BUILD_TOOLS_VERSION=$(cat $FILE | grep 'buildToolsVersion' | sed 's/ //g' | cut -d '"' -f 2)

# Get Min/Max SDK Version
MIN_SDK_VERSION=$(cat $FILE | grep 'minSdkVersion' | grep -o '[[:digit:]]*')
MAX_SDK_VERSION=$(cat $FILE | grep 'compileSdkVersion' | grep -o '[[:digit:]]*')


# Locate unsigned release
UNSIGNED_APK_DIR="$DIR/../demo/build/outputs/apk/release"
UNSIGNED_APK="$UNSIGNED_APK_DIR/demo-release-unsigned.apk"

if [ ! -f $UNSIGNED_APK ]; then
  echo "File not found:"
  echo "$UNSIGNED_APK"
  echo ""
  echo "An unsigned release version is needed. Please build one with Android Studio."
  exit 1
fi

# Get Yubikey PIN
read -p "Please enter your Yubikey PIV pin: " YUBI_PIN

# Zipalign
echo "zipaligning the apk"
echo ""
$ANDROID_SDK/build-tools/$BUILD_TOOLS_VERSION/zipalign 4 \
$UNSIGNED_APK $UNSIGNED_APK.tmp && mv -vf $UNSIGNED_APK.tmp $UNSIGNED_APK

echo ""
echo "signing"
echo ""

# Signing
$ANDROID_SDK/build-tools/$BUILD_TOOLS_VERSION/apksigner sign \
--ks NONE \
--ks-pass "pass:$YUBI_PIN" \
--min-sdk-version $MIN_SDK_VERSION \
--max-sdk-version $MAX_SDK_VERSION \
--provider-class sun.security.pkcs11.SunPKCS11 \
--provider-arg $PKCS11_CFG \
--ks-type PKCS11 \
--out $UNSIGNED_APK_DIR/demo-release-signed.apk \
$UNSIGNED_APK

# Output Verfication
$ANDROID_SDK/build-tools/$BUILD_TOOLS_VERSION/apksigner verify --verbose $UNSIGNED_APK_DIR/demo-release-signed.apk

unset YUBI_PIN
exit 0
