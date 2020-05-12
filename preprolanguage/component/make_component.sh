#!/bin/bash

readonly JAVA_VERSION="${1}"
if [[ $JAVA_VERSION == 1.8* ]]; then
    JRE="jre/"
elif [[ $JAVA_VERSION == 11* ]]; then
    JRE=""
else
    echo "Unkown java version: $JAVA_VERSION"
    exit 1
fi
readonly COMPONENT_DIR="component_temp_dir"
readonly LANGUAGE_PATH="$COMPONENT_DIR/$JRE/languages/sl"
if [[ -f ../native/prepronative ]]; then
    INCLUDE_PREPRONATIVE="TRUE"
fi

rm -rf COMPONENT_DIR

mkdir -p "$LANGUAGE_PATH"
cp ../language/target/prepro.jar "$LANGUAGE_PATH"

mkdir -p "$LANGUAGE_PATH/launcher"
cp ../launcher/target/prepro-launcher.jar "$LANGUAGE_PATH/launcher/"

mkdir -p "$LANGUAGE_PATH/bin"
cp ../sl $LANGUAGE_PATH/bin/
if [[ $INCLUDE_PREPRONATIVE = "TRUE" ]]; then
    cp ../native/prepronative $LANGUAGE_PATH/bin/
fi

touch "$LANGUAGE_PATH/native-image.properties"

mkdir -p "$COMPONENT_DIR/META-INF"
{
    echo "Bundle-Name: PrePro";
    echo "Bundle-Symbolic-Name: com.oracle.truffle.sl";
    echo "Bundle-Version: 20.0.0";
    echo 'Bundle-RequireCapability: org.graalvm; filter:="(&(graalvm_version=20.0.0)(os_arch=amd64))"';
    echo "x-GraalVM-Polyglot-Part: True"
} > "$COMPONENT_DIR/META-INF/MANIFEST.MF"

(
cd $COMPONENT_DIR || exit 1
jar cfm ../prepro-component.jar META-INF/MANIFEST.MF .

echo "bin/prepro = ../$JRE/languages/prepro/bin/prepro" > META-INF/symlinks
if [[ $INCLUDE_PREPRONATIVE = "TRUE" ]]; then
    echo "bin/prepronative = ../$JRE/languages/prepro/bin/prepronative" >> META-INF/symlinks
fi
jar uf ../prepro-component.jar META-INF/symlinks

{
    echo "$JRE"'languages/prepro/bin/prepro = rwxrwxr-x'
    echo "$JRE"'languages/prepro/bin/prepronative = rwxrwxr-x'
} > META-INF/permissions
jar uf ../prepro-component.jar META-INF/permissions
)
rm -rf $COMPONENT_DIR
