<!-- Thanks Square for providing great documentation that I only had to tweak -->
<!-- https://raw.githubusercontent.com/square/leakcanary/master/docs/releasing.md -->

# Releasing PinAuthentication

- Create a local release branch from `master`
```
git checkout master
git pull
git checkout -b release_{{ pin_authentication.next_release }}
```

- Update `VERSION_NAME` (remove `-SNAPSHOT`) in `gradle.properties`
```gradle
VERSION_NAME={{ pin_authentication.next_release }}
```

- Update the current version and next version in `mkdocs.yml`
```
extra:
  pin_authentication:
    release: '{{ pin_authentication.next_release }}'
    next_release: 'REPLACE_WITH_NEXT_VERSION_NUMBER'
```

- Generate the Dokka docs
```
rm -rf docs/pin-authentication
./gradlew pin-authentication:dokka
```

- Update the Demo App if changes were made to documentation in the `PinAuthentication` class:
    ```
    scripts/comments_to_string_resource.sh && gedit scripts/output.txt scripts/output.bak.txt demo/src/main/res/values/strings.xml
    ```
    - Tweak the strings to 45-50 characters per line & add headers
        - ex: `\n//////////// Listeners ////////////\n`
    - Copy/Paste new strings into `demo/src/main/res/values/strings.xml` resource file fields
        - `text_view_settings_features`
        - `text_view_controller_features`
    - Save files
    - Delete `output.bak.txt`
    ```
    rm -rf scripts/output.bak.txt
    ```

- Update `docs/changelog.md` after checking out all changes:
    - <a href="https://github.com/05nelsonm/pin-authentication/compare/{{ pin_authentication.release }}...master" target="_blank">compare changes</a>

- Take one last look
```
git diff
```

- Commit all local changes and PGP sign
```
git commit -S -am "Prepare {{ pin_authentication.next_release }} release"
```

- Perform a clean build
```
./gradlew clean
./gradlew build
```

- Sign the Demo App release apk
    ```
    scripts/sign_demo_release_build.sh
    ```

- Install the Demo App's release.apk and ensure it works properly.

- Create a PGP signed tag, and push it
```
git tag -s {{ pin_authentication.next_release }} -m "Release v{{ pin_authentication.next_release }}"
git push origin {{ pin_authentication.next_release }}
```

- Make sure you have valid credentials in `~/.gradle/gradle.properties` to sign and upload the artifacts
```
SONATYPE_NEXUS_USERNAME=<Your Username>
SONATYPE_NEXUS_PASSWORD=<Your Password>

signing.gnupg.homeDir=/home/matthew/.gnupg/
signing.gnupg.optionsFile=/home/matthew/.gnupg/gpg.conf
signing.gnupg.keyName=0x61471B8AB3890961
```

- Upload the artifacts to Sonatype OSS Nexus
```
./gradlew uploadArchives --no-daemon --no-parallel
```

- Release to Maven Central
    - Login to Sonatype OSS Nexus: <a href="https://oss.sonatype.org/#stagingRepositories" target="_blank">oss.sonatype.org</a>
    - Click on **Staging Repositories**
    - Scroll to the bottom, you should see an entry named `iomatthewnelson-XXXX`
    - Check the box next to the `iomatthewnelson-XXXX` entry, click **Close** then **Confirm**
    - Wait a bit, hit **Refresh**, until the *Status* for that column changes to *Closed*.
    - Check the box next to the `iomatthewnelson-XXXX` entry, click **Release** then **Confirm**

- Merge the release branch to master
```
git checkout master
git pull
git merge --no-ff -S release_{{ pin_authentication.next_release }}
```

- Update `VERSION_NAME` (increase version and add `-SNAPSHOT`)  and `VERSION_CODE` in `gradle.properties`
```gradle
VERSION_NAME=REPLACE_WITH_NEXT_VERSION_NUMBER-SNAPSHOT
VERSION_CODE=INCREMENT
```

- Commit your changes and sign with PGP keys
```
git commit -S -am "Prepare for next development iteration"
```

- Push your changes
```
git push
```

- Wait for the release to become available on <a href="https://repo1.maven.org/maven2/io/matthewnelson/pin-authentication/pin-authentication/" target="_blank">Maven Central</a>, then:
    - Redeploy the docs:
        - `pipenv shell`
        - `mkdocs serve` to check locally
        - `mkdocs gh-deploy` to deploy
        - `exit`
    - Go to the <a href="https://github.com/05nelsonm/pin-authentication/releases/new" target="_blank">Draft a new release</a> page,
      enter the release name ({{ pin_authentication.next_release }}) as tag and title, and have the description
      point to the changelog. You can find the direct anchor URL from the
      <a href="https://05nelsonm.github.io/pin-authentication/changelog" target="_blank">Change Log</a>
      page on the doc site.
    - Upload the `demo-release-signed.apk` to assets

- Release the Demo App to the <a href="https://play.google.com/apps/publish/" target="_blank">Play Store</a>
