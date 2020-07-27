# Introduction

mVis is a visual analytics tool for visualising multi-dimensional datasets using various views including scatterplots and parallel coordinates.
The mVis system consists of four data visualisation views and a panel to control partitions. mVis is written in Java and uses JavaFX for its user interface.
It supports traditional mouse and keyboard as well as multi-touch user input. The four linked exploratory data visualisations built into mVis are: SPLOM, scatterplot, similarity map (projection by PCA, MDS, and t-SNE), and parallel
coordinates plot. All the visualisations are connected through standard brushing and linking, so selections and changes in one view are reflected in all other views.
Moreover, the user can close, rearrange, or enlarge any view.

# Tools

  - Recommended compiler: Java 11.02 (OpenJDK)
  - Recommended IDE: Netbeans 10
  - mVis is using Maven 3.6

# Compiling

You can either compile by IDE or command line (maven).

## Netbeans

Import these projects from dependencies folder into the IDE (e.g. Netbeans 10):
  - AnchorFX
  - ComplexDataObject
  - DMandML

Later, import the main project from mvis-src and right click then click on the build with dependencies. Run the Main.java in at.tugraz.cgv.multiviewva.javafxapplication package.

## Command Line

To build the system with maven, first build each of the dependencies:

```
cd dependencies/AnchorFX-master
mvn clean install -U
```

```
cd dependencies/ComplexDataObject-master
mvn clean install -U
```

```
cd dependencies/DMandML-master
mvn clean install -U
```

Then build the system itself:

```
cd mvis-src
mvn clean install -U
```

This will produce a jar file in the target folder:

  mvis-src\target\mVis-java-1.0-SNAPSHOT.jar

# Creating Build for OS

After Java 11, one can directly export Java to native formats (e.g. .exe). Jpackage is the tool to perform this, and then the user don't need to have JDK installed anymore. You can find Jpackage here:

https://jdk.java.net/jpackage/

You also need native packager for Linux, Mac and Windows. For Windows you can use one of these two options:

- exe — Inno Setup, a third-party tool, is required to generate exe installers
- msi — Wix, a third-party tool, is required to generate msi installers

[Inno Setup](http://www.jrsoftware.org/isinfo.php)
[Inno Setup Download](http://www.jrsoftware.org/isdl.php)

[Wix Toolset](http://wixtoolset.org)
[Wix Toolset Downloads](http://wixtoolset.org/releases/)

For making a runnable image run this command in cmd:

```
cd mvis\mvis-src
"C:\Program Files\Java\jdk-13\bin\jpackage.exe" create-image -o "C:\Users\chegini\Documents\mVis" -i target -n "mVis" --main-class at.tugraz.cgv.multiviewva.javafxapplication.Main --icon "C:\Users\chegini\Documents\mVis\cgv.ico" --main-jar target\mVis-java-1.0-SNAPSHOT.jar
```

For making an installer file run this one:

```
cd mvis\mvis-src
"C:\Program Files\Java\jdk-13\bin\jpackage.exe" create-installer --installer-type exe --win-shortcut --win-menu --win-dir-chooser -i "target" -o "C:\Users\chegini\Documents\mVis" -n "mVis" --main-class at.tugraz.cgv.multiviewva.javafxapplication.Main --icon "C:\Users\chegini\Documents\mVis\cgv.ico" --main-jar target\mVis-java-1.0-SNAPSHOT.jar
```

The mVis.ico file can be found in the repository.