# Builtin Erstellen

Vorbedingung: Das Projekt muss bereits ausgecheckt und lauffähig sein.

## Was ist ein Builtin
Unter builtins werden alle Funktionen zusammengefasst, die PrePro bereits von Haus aus mitliefert (die in die Sprache "eingebaut" sind).
Das bedeutet, sie können direkt in jedem Skript aufgerufen werden.


## Welche Builtins gibt es

Es gibt zwei Sorten von Builtins: Interne und externe.

Interne Builtins sind immer verfügbar. Externe können von der jeweiligen Laufzeitumgebung hinzugefügt werden.  
Zum Zeitpunkt des Erstellens dieses Dokuments gibt es nur interne Builtins.

Da die Liste der existierenden Builtins schnell veralten würde, hier wie man die registrierten Builtins findet:  
In `PreProContext#installBuiltins` werden die Builtins registriert.  
Daher können alle Builtins dort direkt eingesehen werden.


## Neue Builtins anlegen

Im Nachfolgenden wird das Builtin für die Sinusfunktion `sin(x)` angelegt.

### Builtin Klasse Erzeugen

Um ein neues Builtin anzulegen, muss eine abstrakte Klasse erstellt werden, die `PreProBuiltinNode` erweitert.  
Gleichzeitig wird der Name des Builtins (und damit auch der Funktionsname, unter dem es aufgerufen wird) mittels der `@NodeInfo` Annotation übergeben.
```java
@NodeInfo(shortName = "sin")
public abstract class PreProSinBuiltin extends PreProBuiltinNode {
}
```

### Projekt kompilieren

Das Erweitern der PreProBuiltinNode sorgt dafür, dass beim nächsten Kompilieren der Annotationsprozessor von Truffle eine neue Klasse erzeugt. Diese hat den Namen der Builtin Klasse plus die Endung `Factory`.  
Falls die die Klasse nicht gefunden wird, die IDE darauf anweisen, den `target/generated-sources/annotations` Ordner neu zu indizieren. Je nach build Einstellung kann es möglich sein, dass über Maven `mvn compile` gebaut werden muss.

```java
@GeneratedBy(PreProPrintBuiltin.class)
public final class PreProPrintBuiltinFactory implements NodeFactory<PreProPrintBuiltin> {
	//Generierter Code
}
```

### BuiltinFactory registrieren

Eine Instanz der Factory-Klasse muss nun im `PreProContext` registriert werden.  
Das Registrieren sorgt dafür, dass das Builtin benutzt werden kann.  

Die Factory-Klassen sind nach dem Singleton Pattern aufgebaut und besitzen daher eine `getInstance()` Methode.  
Registriert werden die Instanzen innerhalb von `PreProContext#installBuiltins`.

```java
private void installBuiltins() {
    //... Bereits existierende Builtins ...//

    installBuiltin(PreProSinBuiltinFactory.getInstance());
}
```

### Spezialisierungen erstellen

Das bisher erzeugte Builtin kann zwar benutzt werden, besitzt allerdings noch keine wirkliche Funktionalität.  
Nun muss die Builtin Klasse dahingehend erweitert werden, dass die entsprechenden Methodensignaturen unterstützt werden.

Hierfür werden Instanzmethoden innerhalb des Builtins geschrieben, und mit der Annotation `@Specialization` versehen.  
Die Annotation sorgt dafür. Dass die vom Annotationsprozessor erzeugte Klasse die für die Argumente passende Implementierung wählt. 

Nur mit `@Specialization` versehene Methoden werden berücksichtigt!.  
Des Weiteren ist zu beachten, dass die Reihenfolge der Methoden für den Annotationsprozessor eine Rolle spielt. PrePro wird die verschiedenen Signaturen von oben nach unten "durchprobieren". Daher müssen spezifischere Methodensignaturen weiter oben stehen als weniger spezifische. Der Annotationsprozessor gibt bei Missachten lediglich Warnungen auf der Konsole aus, die leicht überlesen werden können.

Eine Beispielhafte Methode sieht so aus.

```java
@Specialization
public PreProConstant sin(PreProConstant constant) {
    return new PreProConstant(Transforms.sin(constant.timeSeries(), true));
}
```


#### Arbeiten mit Elementweisen Transformierungen

Für viele Builtins muss eine Operation auf jedes Element der Matrix angewendet werden.  
Für die Standard-Funktionen bietet ND4J hierfür bereits Operatoren an. Diese sind in der Klasse `org.nd4j.linalg.ops.transforms.Transforms` zu finden.


### Packagen und Ausführen

Die bisherigen Änderungen sind zwar bisher im Code, aber noch nicht im Jar. Dafür ein `mvn clean package` ausführen, um alles zu packagen, dann kann man auch damit testen.


### Tests

Und am Ende natürlich noch Tests schreiben :smile: