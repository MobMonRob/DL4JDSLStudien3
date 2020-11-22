#!/usr/bin/env bash
if [[ $PREPRO_BUILD_NATIVE == "false" ]]; then
    echo "Skipping the native image build because PREPRO_BUILD_NATIVE is set to false."
    exit 0
fi
"$JAVA_HOME"/bin/native-image \
    --macro:truffle --no-fallback --initialize-at-build-time \
    -cp ../language/target/prepro.jar:../launcher/target/prepro-launcher.jar \
    com.oracle.truffle.prepro.launcher.PreProMain \
    prepronative
