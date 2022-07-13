# STP-Beispiele

## Hallo-Welt-Skript

Ausgabe von "Hallo, Welt!":

`"Hallo, Welt!"`

`= "Hallo, Welt!"`

## Konkatenierung

Einfache Konkatenierung:

`(join "Der Wert beträgt: " (+ 41 1))`

`= "Der Wert beträgt: 42"


Konkatenierung mit Sonderoptionen:

`(joinl ("Tick" "Trick" "Track") ", " " und ")`

`= "Tick, Trick und Track"`

## Schleifen und Verzweigungen

Wenn-Sonst:

`(if (= 1 1) "wahr" "falsch")`

`= "wahr"`

`(if (= 0 1) "wahr" "falsch")`

`= "falsch"`

`(if (= 1 1) (+ 2 2) (+ 1 1))`

`= 4`

Ausgabe aller druckbaren ASCII-Zeichen:

`(codepoints-to-string 32..126)`

```= " !"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz{|}~"```

## Funktionen

Definierung einer Inkrementierungsfunktion und Aufruf derselben mit 41 als erstem Argument:

`{x -> (+ x 1)}(41)`

`= 42`

### Überladungen

Nutzung von Überladungen, um herauszufinden, ob eine Zahl gerade oder ungerade ist:

`(Function {x:(= (mod x 2) 0) -> "gerade"} {x:(= (mod x 2) 1) -> ungerade})(42)`

`= "gerade"`

`(Function {x:(= (mod x 2) 0) -> "gerade"} {x:(= (mod x 2) 1) -> ungerade})(41)`

`= "ungerade"`