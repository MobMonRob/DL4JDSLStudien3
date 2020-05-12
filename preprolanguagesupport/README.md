# PreproLanguageSupport

Project to create a NetBeans Plugin for file type recognition and syntax highlighting of PrePro.

To create the .nbm file run

```shell
mvn package 
```
in the main directory. 

The .nbm file can than be found in the target folder. To install the Plugin open the NetBeans IDE. Go to Tools > Plugins.
Select the Tab Downloaded and add the .nbm file from the target folder by clicking the Button AddPlugins...
Install the Plugin by following the installation dialogue.

![Install Plugin](./docs/InstallPlugin.png)

You can check that the Plugin is working by opening a PrePro-File. If it is preceded by the PrePro-Icon everything is fine. 
Eventually you might need to restart your IDE.

![Check Plugin](./docs/CheckPreProIcon.png)
