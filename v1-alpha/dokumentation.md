# STP-Dokumentation

<head>
    <meta name='robots' content='noindex,nofollow,noarchive' />
</head>

## Vorwort

Bla

## Zweck

Diese Dokumentation definiert und erläutert STP, anhand von Definitionen, Erklärungen und Beispielen.

## Konforme Implementierung

Eine konforme STP-Implementierung muss das in dieser Dokumentation beschriebene Verhalten von STP nachbilden können.
Sie muss Quelltext entsprechend des aktuellen Unicode-Standards behandeln.
Es ist ihr erlaubt, das hier beschriebene Verhalten zu ergänzen.
Sie darf nicht hier beschriebenes Verhalten umdefinieren.
Alles weitere ergibt sich aus den Beschreibungen in dieser Dokumentation.

## Ausdrücke

1. Ein Ausdruck ist entweder ein Atom oder etwas, was als Liste dargestellt werden kann, deren Elemente wiederum Ausdrücke sind.
2. Ein Atom ist eine Zeichenkette, die einen Text, eine Zahl, einen Wahrheitswert, einen Namen oder ähnliches hinreichend "verlustfrei" darstellen kann. Ein Atom kann keine Unterausdrücke enthalten. Ein Atom ist daher aus Sicht von STP "unteilbar".
3. Ein Atom ist entweder eine Zahlenliteral, ein Bezeichnerliteral oder ein Zeichenkettenliteral.
4. Ein Zahlenliteral hat entweder das Format sz, bz, sz.z oder sz/z, wobei z eine Kette beliebiger Länge von Ziffern ist, b entweder leer, 0b, 0o, 0d oder 0x ist, s entweder leer oder - (Bindestrichminus) ist. Folgende sind unter anderem Zahlenliterale
    1. 0xAFFE (Hexadezimalzahl, Basis 16, dezimal 45054)
    2. 0d1000 (Duodezimalzahl, Basis 12, dezimal 1728)
    3. 0o1234 (Oktalzahl, Basis 8, dezimal 668)
    4. 0b1111 (Binärzahl, Basis 2, dezimal 15)
    5. -10
    6. 987654321
    7. 355/113
    8. 0.75
5. Ein Zeichenkettenliteral besteht aus beliebigen Zeichen, die nicht doppelte Anführungszeichen sein dürfen, die in doppelte Anführungszeichen gesetzt werden. `"..."`, `"Hallo, Welt!"`, `"( Eine kaputte.Liste"`
6. Ein Bezeichnerliteral besteht aus den Zeichen a-b, A-B, 0-9 und dem Bindestrich, wobei das erste Zeichen keine Zahl sein darf.
7. Die Darstellung einer Liste beginnt mit einer öffnenden Klammer und endet mit einer schließenden Klammer, die einzelnen Elemente der Liste werden innerhalb dieser Klammern durch Leerzeichen getrennt. Damit ein Element als solches erkannt wird, muss es zwingend mindestens ein Zeichen enthalten. Daraus folgt, dass `(abc d )` wie `(abc d)` und `( abc d )` interpretiert wird. Jegliche Whitespace-Zeichen werden vor der Interpretation in Leerzeichen umgewandelt. Dadurch Aufeinanderfolgende in ein einzelnes.
8. Eine leere Liste wird mit `()` dargestellt.
9. Jeder Listenausdruck kann ein Präfix und ein Suffix enthalten. Das Präfix ist der Liste vorangestellt und wird als erstes Element in diese eingefügt, sodass: `a(b c)` = `(a b c)` gilt. `a` muss hierbei entweder ein Atom oder eine einzelne Liste sein, um Mehrdeutigkeit zu vermeiden. In dieser Hinsicht mehrdeutige Ausdrücke sind unzulässig. Es gibt für jeden Datentypen eine Funktion gleichen Namens, die die Operanden als eigene Funktion evaluiert und das Ergebnis in den entsprechenden Typ umwandelt, sofern möglich.
10. Der Doppelpunkt `:` sowie das Komma `,` am Ende von Ausdrücken werden ignoriert. Diese sollen jedoch ausschließlich in Map- bzw. Objektausdrücken verwendet werden, um eine bessere Übersicht der Schlüssel-Wert-Paare zu erlauben.
Damit ist
`Map(vorname: "Max", nachname: "Mustermann", geburtsjahr: 1950)`
gleichbedeutend mit
`(Map vorname "Max" nachname "Mustermann" geburtsjahr 1950)`
Es wird sich vorbehalten, diesen Zeichen in diesem Zusammenhang in Zukunft eine tatsächliche syntaktische Bedeutung/Erfordernis zu verleihen.
In allen anderen Zusammenhängen (id est Ausdrücke, die nicht Bestandteil eines Mapkonstruktors sind) darf der Parser Ausdrücke ablehnen, die mit diesen beiden Zeichen enden.
11. Sofern nicht anders bestimmt, evaluieren Ausdrücke zu sich selbst.
12. `true`, `false`, `null` und `void` sind Literale, die jeweils für die booleschen Werte wahr und falsch stehen, sowie für den Nichtswert (null) und den Leerwert (void)

## Blockausdrücke

1. Blöcke sind Ausdrücke, die mit geschweiften statt runden Klammern beginnen und enden.
2. Die Argumente eines Blocks werden von links nach rechts interpretiert, wobei Seiteneffekte früherer Argumente sich auf die Interpretation späterer Argumente auswirken können.
3. Ein Block gibt sein letztes Argument interpretiert zurück.

## Funktionen & Methoden

1. Eine Funktion ist entweder eine Methode oder ein Bündel mehrerer Methoden.
2. Eine Methode besteht aus einer Parameterliste und einer Liste von Ausdrücken (Methodenbauch).
3. Ein Funktionsaufruf ist eine Liste, deren erstes Element zu einer Funktion evaluiert. Beim Aufruf der Funktion werden dieser Argumente entsprechend ihren Parametern übergeben und die Liste von Ausdrücken der Methode nacheinander interpretiert, wobei das letzte Resultat dieser Interpretationen das Resultat des Funktionsaufrufes ist.
   1. Erhält eine Methode durch einen Funktionsaufruf weniger Argumente als ihre Parameter erwarten, so sind die fehlenden vom Wert `void`.
4. Ein Methodenobjekt kann mit einem Literal beschrieben werden: `{P1 P2 ... Pn -> A1 A2 ... An}`, wobei Pi ein Parameter ist und Ai ein beliebiger Ausdruck.
5. Enthält eine Funktion mehr als eine Methode, dann wird bei einem Aufruf die erste Methode ausgewählt,
   deren Beschränkungen von den Argumenten des Aufrufes erfüllt werden. Eine Funktion mit mehreren Methoden wird mit `(Function F0 F1 ... Fn)` erzeugt, wobei Fi eine Funktion ist.
6. Der Pseudo-Parameter `_` kennzeichnet, dass ein Argument erwartet wird, aber in dem Methodenbauch keine Verwendung findet.
   Es können beliebig viele Unterstriche als Parameter Verwendung finden. Dieser Pseudo-Parameter darf mit `@constrain` beschränkt werden.
7. Der Pseudo-Parameter `@sealed` blockiert weitere Argumente. Er ist daher pro Parameterliste einmalig, ist ihr letztes Element und "akzeptiert" nur `void`-Werte.
   `{@sealed -> 42}` ist eine Methode, die keine Argumente akzeptiert, während `{zahl @sealed -> 42}` ausschließlich ein einziges Argument akzeptiert.

## Variablen

Es ist möglich, an einen "Bezeichner" einen Ausdruck zu binden. Dieser Ausdruck kann ein Wert sein. Der Bezeichner ist dann der "Name" des Wertes und steht symbolisch für diesen. Durch erneutes Binden an den Bezeichner wird der Wert faktisch überschrieben, sodass das ganze in Gesamtheit einer Variable gleicht. Auf diese Weise erzeugte Variablen sind "lokal" und können nur im Code-Block und ihm untergeordneten Code der Binde-Funktion benutzt werden.

## Datentypen

Es gibt folgende atomare primitive Datentypen:

| Kürzel | Code | Deutsch | Wertebereich | Bemerkung |
| ------ | ---- | ------- | ------------ | --------- |
| B | `Boolean` | Wahrheitswert | false, true | |
| Q | `Rational` | Rationale Zahl | -2^63 .. 2^63-1 | Verhältnis zweier Ganzzahlen |
| I | `Identifier` | Bezeichner | n.A. | Dient als Name für Variablen, Parameter usw. |
| S | `String` | Zeichenkette | n.A. | Stellt Text dar |
| N | `Null` | Nichtstyp | null | kein Wert |
| V | `Void` | Leertyp | void | keine Rückgabe |

Es gibt folgende strukturierte primitive Datentypen:

| Kürzel | Code | Deutsch | Wertebereich | Bemerkung |
| ------ | ---- | ------- | ------------ | --------- |
| L | `List`| Liste | Eine Liste beliebig vieler Ausdrücke |
| Er | `Error` | Fehler | Eine Fehlermeldung |
| F | `Function` | Funktion | Eine anonyme Funktion |
| M | `Map` | Zuordnungstabelle | Ein Schlüssel-Wert-Verzeichnis |

## Die Rationale Zahl

Rationale Zahlen (Verhältnisse zweier ganzer Zahlen) sind die Grundlage mathematischer Berechnungen in STP. STP akzeptiert jede Darstellung nach dem Schema a/b und -a/b , wobei a und b ganze Zahlen dargestellt in der Basis 10 und zwischen 0 und 2^63-1 sein müssen. Intern werden diese gekürzt und dafür gesorgt, dass der Nenner nie negativ ist. Dadurch ist das Vorzeichen des Zählers automatisch das Vorzeichen des gesamten Bruchs.

## Beschränkungen und Funktionsüberladungen

Standardmäßig akzeptiert STP jeden Ausdruck als Funktionsargument und kann jeden Ausdruck jeder Art als Resultat zurückgeben. Um den Bereich erlaubter Argumente einzuschränken, ist es möglich Beschränkungen, sogenannte 'Constrain's zu verwenden. Diese stellen eine Bedingung dar, die das übergebene Argument erfüllen muss, damit die entsprechende Funktion ausgewertet werden kann; und sind entsprechend Parameter-Annotationen.

Beschränkungen sind beliebige Bedingungen, die von den Argumenten eines Funktionsaufrufes erfüllt werden müssen.
Beispiel:
`{(constrain x (= (mod x 2) 0)) -> x}` bzw `{x:(= (mod x 2) 0) -> x}`
Beschränkung für den Parameter x, die ausschließlich grade Zahlen als Argumente erlaubt.

Für den Aufruf von Funktionen in Form einer Funktionsliste mit Beschränkungen gilt, dass alle in der Liste enthaltenen Funktionen auf Erfüllung der Bedingungen getestet werden. Erfüllen die Argumente alle Bedingungen einer Funktion, so wird diese ausgeführt. Wenn die Argumente die Bedingungen mehrerer benannter Funktionen erfüllen, so ist das Resultat das Ergebnis der ersten Funktion, die die Beschränkungen erfüllt. Gibt es keine Funktion, die die Bedingungen erfüllt, wird ein Fehler ausgegeben.

Beschränkt werden kann jedes Element der Parameterliste einschließlich `_` und `@args`, nicht aber `@void`.

## Operatoren

1. Der Indexoperator `a[b]` entspricht `(get a b)`
2. Der Mitgliedsoperator `a.b` entspricht `(get-prop a b)`
3. Der Kurzfunktionsausdruck `{A... -> B...}` entspricht `(Function (A...) (begin B...))`
4. Der Bereichsoperator `a..b` entstpricht `(range a b)`
5. Der Elvisoperator `a?:b` entspricht `(coalesce a b)`
6. Der Operator der sicheren Navigation `a?.b` entspricht `(safe-prop a b)`
7. Ein Ausdruck, der mit ... beginnt, wird transformiert zu ...
    - `'x` -> `(quote x)`
    - `?x` -> `(eval x)`
    - `` `x `` -> `(quasiquote x)`
    - `,x` -> `(unquote x)`
8. `(a . b)` wird transformiert zu `(a b)`
9. Die Zeichen `` ' ? ` `` und `,` innerhalb von Ausdrücken sind unzulässig.

## Maps & Objekte

1. Maps sind assoziative Felder, 
2. Ist der Schlüssel `@extends` auf einen Wert gesetzt, der zu einer anderen Map oder einer Liste von Maps interpretiert wird, so "erbt" die Map von dieser(/n) anderen Map(s) ihre Eigenschaften (Schlüssel-Wert-Zuordnungen).

## Grundfunktionen

Legende zur Notation:

- A/B steht für eine Alternative: Entweder A oder B
- T... steht für beliebig viele Argumente des Typs T
- Tn, wobei n eine Zahl ist, steht für das n-te Argument des Typs T
- Ai in der Beschreibung steht für A in der Eingabe, Ao für A in der Ausgabe
- Cn steht für eine Beschränkung, die je nach Implementation ein primitiver 0. Datentyp, eine Liste oder eine Funktion ist
- Lp steht für ein logisches Prädikat, welches B als Ergebnis hat
- St steht für eine Struktur
- Z steht für eine rationale Zahl, die zugleich eine Ganzzahl ist
- N steht für eine rationale Zahl, die zugleich eine Ganzzahl >= 0 ist
- L(T) steht für eine Liste, deren Elemente alle vom Typ T sind
- P(T1 T2) steht für ein Paar, dessen erstes Element den Typ T1 hat und dessen zweites Element den Typ T2 hat
- [A]:B steht dafür, dass B der Default-Wert bei einem optionalen A ist. Wird kein 0. A angegeben, wird B angenommen
- {A B} steht dafür, dass für A die Beschränkung B gilt
- name=A steht dafür, dass name vom Typ A ist

| Name | Eingabetypen | Ausgabetypen | Beschreibung | Version | Definition |
| ---- | ------------ | ------------ | ------------ | ------- | ---------- |
| Grundprimitive | | | | | |
| get-version | V | S | STP-Version | 1 |  |
| decimal | Q | S | Dezimale Repräsentation von Q. Aus 1/4 wird "0.75" | 1 |  |
| copy-deep | E | E | Gibt eine Tiefenkopie des evaluierten Ei zurück | 0 |  |
| coalesce | E1 E2 | E | Gibt E1 zurück, sofern dieses nicht null ist, sonst E2 | 1 |  |
| list | E... | L | Liste aller evaluierten E... | 0 |  |
| quote | E | E | Ei in Rohform | 0 |  |
| eval | E | E | Evaluiertes Ei | 0 |  |
| quasiquote | E | E | Wie quote, nur dass innere Ausdrücke mit unquote evaluiert werden | 0 |  |
| unquote | E | E | Evaluiertes Ei | 0 |  |
| ignore | E... | V | Dient als Kommentar | 1 |  |
| is-atom | E | B | Ist E ein Atom? | 0 |  |
| type-of | E | S | Name des Datentyps von E | 0 |  |
| instanceof | E S | B | Hat E den Datentyp S ? | 0 |  |
| begin | E... | E | Evaluiert nacheinander alle E...i und gibt letztes Ergebnis aus | 1 |  |
| id | E | E | Identitätsfunktion | 1 |  |
| return | E | E | Zur Zeit wie id. Bitte nur dort verwenden, wo eine Funktion abgebrochen werden soll. | 0 |  |
| exit | V | V | Beendet das Skript | 1 |  |
| primitive | S L | E | Übergibt an die Primitivfunktion S die Argumente L | 1 |  |
| Parsingprimitive | | | | | |
| tokenize | S | L | Liste von Tokens | 1 |  |
| is-valid-expression | S | B | Ist S ein valider Ausdruck? | 1 |  |
| parse | S | E | Parst S | 1 |  |
| Kontextprimitive | | | | | |
| get-primitives | V | L | Liste der Bezeichner aller primitiven Funktionen | 0 | |
| get-context | V | M | Aktueller Kontext | 1 | |
| set-context | M | V / E | Überschreibt den aktuellen Kontext | 1 | |
| new-subcontext | E | E | Evaluiert Ei in einem neuen Unterkontext | 1 | |
| context-of | E | M | Kontext, der den Schlüssel E definiert | 1 |  |
| Symbolprimitive | | | | | |
| def | schluessel1=E wert1=E ... schluesselN=E wertN=E | E | Bindet Werte an Schlüssel im aktuellen Kontext | 0 |  |
| overload | schluessel1=E wert1=F ... schluesselN=E wertN=F | E | Sofern der Schlüssel bereits in einem höheren oder diesem Kontext definiert ist, wird dieser mit einem neuen Wert überschieben. Andernfalls wird der Wert an einen neuen Schlüssel im aktuellen Kontext gebunden | 1 |  |
| overwrite | schluessel1=E wert1=E ... schluesselN=E wertN=E | E | Sofern der Schlüssel bereits in einem höheren oder diesem Kontext definiert ist, wird dieser mit einem neuen Wert überschieben. Andernfalls wird der Wert an einen neuen Schlüssel im aktuellen Kontext gebunden | 1 |  |
| let | schluessel1=E wert1=E ... schluesselN=E wertN=E ausdruck=E | E | Bindet Werte an Schlüssel in einem Unterkontext und gibt Resultat von ausdruck zurück | 0 |  |
| call | F args=E/L | E | Ruft die Funktion F mit den Argumenten args auf | 0 |  |
| Konstruktoren |  |  |  |  |  |
| Error |  | Er |  | 0 |  |
| Function |  | F |  | 0 |  |
| Map |  | M |  | 0 |  |
| List |  | L |  | 0 |  |
| Rational |  | Q |  | 0 |  |
| as-list | St | L | Wandelt St in seine Listendarstellung um | 0 |  |
| String |  | S |  | 0 |  |
| Mapfunktionen |  |  |  |  |  |
| put-prop | M E1 E2 | M | Fügt in M den Wert E2 für den Schlüssel E1 ein | 1 |  |
| remove-prop | M E | M | Entfernt das E in M | 1 |  |
| get-prop | M E | E | Wert des Schlüssels Ei in M | 1 |  |
| safe-prop | M E | E | Wert des Schlüssels Ei in M | 1 | Safe-Navigation |
| IO-Funktionen |  |  |  |  |  |
| date-and-time | V | L | Aktuelle Zeit im Format `(Date-And-Time Y M D h m s)` | 0 |  |
| current-time-millis | V | N | Aktuelle Zeit in Millisekunden | 1 |  |
| io-exists | S... | B | Gibt zurück, ob die in S... genannten Dateien im Skriptverzeichnis existieren | 1 |  |
| io-read-bytes | E | L(N) | Liest Datei mit dem Namen E ein und gibt eine Liste der Bytes aus | 0 |  |
| in | E... | E | Ruft die Eingabe-Prozedur mit den Operanden als Argumente auf und gibt deren Ergebnis zurück | 1 |  |
| out | E... | E | Ruft die Ausgabe-Prozedur mit den Operanden als Argumente auf und gibt deren Ergebnis zurück | 1 |  |
| printc-to-console | E | V | Schreibt den Wert von E in die Konsole | 1 |  |
| Schleifen und Verzweigungen |  |  |  |  |  |
| cond | L(L(B E)...) | E | Prüft, ob ein B der Paare wahr ist und gibt das entsprechende E aus. Ist keine wahr, wird false ausgegeben | 0 |  |
| if | B E1 E2 | E | Wenn B wahr ist, wird E1 ausgegeben, sonst E2 | 0 |  |
| while | B E | E | E wird evaluiert, solange B wahr ist. Das letzte Ergebnis wird ausgegeben | 0 |  |
| for | E1 B E2 E3 | E | Evaluiert E1. Evaluiert danach solange E3 und dann E2, wie B wahr ist | 0 |  |
| foreach | St F | L | Wendet F auf jedes Element in St an. Dabei sind die übergebenen Argumente: Wert, Index, Struktur | 0, 1 |  |
| pipe | E [F1...Fn] | E | Übergibt E als erstes Argument an F1 und deren Ergebnis als erstes Argument an F2 bis Fn.<br />Das Ergebnis von Fn ist das Ergebnis von pipe.<br />Statt F darf auch E benutzt werden, wobei sich hier quotierte E empfehlen. Dann wird x als Resultat der vorangegangenen Funktion definiert. | 1 |  |
| multi-call | F(>= A1...An) L1...Ln | L | Evaluiert F so viele Male, wie die Länge der längsten Liste. Sei Index ein Zähler der Durchläufe, so sind die an F übergebenen Argumente im Index-ten Durchlauf L1[Index] bis Ln[Index] sowie Index. Das Ergebnis von multi-call ist eine Liste aller Resultate | 1 |  |
| Arithmetik |  |  |  |  |  |
| + | Q... | Q | Summiert alle Qs | 0 |  |
| - | Q1 ... Qn | Q | Subtrahiert Q2 ... Qn von Q1 | 0 |  |
| * | Q... | Q | Multipliziert alle Qs | 0 |  |
| / | Q... | Q / Er | Dividiert Q1 durch Q2 ... Qn | 0 |  |
| power | Q... | Q | Q1 hoch Q2 ... hoch Qn.<br />Nota bene: *(sqrt x) = (power x 1/2) | 1 |  |
| truncate | Q | Z | Entfernt den Nachkommaanteil von Q | 0 |  |
| sin | Q | Q | Sinus von Qi | 0 |  |
| cos | Q | Q | Kosinus von Qi | 0 |  |
| tan | Q | Q | Tangens von Qi | 0 |  |
| asin | Q | Q | Arcus-Sinus von Qi | 0 |  |
| acos | Q | Q | Arcus-Kosinus von Qi | 0 |  |
| atan | Q | Q | Arcus-Tangens von Qi | 0 |  |
| abs | Q | Q | Betrag von Qi | 1 |  |
| ceiling | Q | Q | Aufgerundetes Qi | 1 |  |
| floor | Q | Q | Abgerundetes Qi | 1 |  |
| log | [Q1]:10 Q2 | Q | Logarithmus von Q2 zur Basis Q1 | 1 |  |
| round | Q | Q | Kaufmännisch gerundetes Qi | 1 |  |
| max | Q... | Q | Größter Wert von Q... | 1 |  |
| min | Q... | Q | Kleinster Wert von Q... | 1 |  |
| mod | Q1 Q2 | Q | Rest der Division Q1 durch Q2 | 1 |  |
| max-num | V | Z | Maximale durch den Typ Number darstellbare Zahl | 1 |  |
| min-num | V | Z | Minimale durch den Typ Number darstellbare Zahl | 1 |  |
| max-int | V | Z | Maximaler durch den Typ Number darstellbarer Integer | 1 |  |
| min-int | V | Z | Minimaler durch den Typ Number darstellbarer Integer | 1 |  |
| numerator | Q | Z | Zähler der Bruchdarstellung von Q | 1 |  |
| denominator | Q | N | Nenner der Bruchdarstellung von Q | 1 |  |
| Bitweise Primitive |  |  |  |  |  |
| bitand | Z1 Z2 | Z | Und der Bits von Z1 und Z2 | 0 |  |
| bitior | Z1 Z2 | Z | Inklusives Oder der Bits von Z1 und Z2 | 0 |  |
| bitxor | Z1 Z2 | Z | Exklusives Oder der Bits von Z1 und Z2 | 0 |  |
| bitnor | Z1 Z2 | Z | Nicht Oder der Bits von Z1 und Z2 | 0 |  |
| biteqv | Z1 Z2 | Z | Äquivalenz der Bits von Z1 und Z2 | 0 |  |
| bitash | Z1 Z2 | Z | Arithmetische Verschiebung der Bits von Z1 um Z2 Stellen | 0 |  |
| bitlsh | Z1 Z2 | Z | Logische Verschiebung der Bits von Z1 um Z2 Stellen | 0 |  |
| bitcount | Z | Z | Anzahl aller 1-Bits in Zi | 0 |  |
| bitnot | Z | Z | Negiert die Bits von Zi | 0 |  |
| Vergleiche |  |  |  |  |  |
| = | E1 E2...En | B | Sind alle E2...En gleich E1 ? | 0 |  |
| != | E1 E2...En | B | Sind alle E2...En ungleich E1 ? | 0 |  |
| > | E1 E2...En | B | Ist E1 größer als alle E2...En ? | 0 |  |
| < | E1 E2...En | B | Ist E1 kleiner als alle E2...En ? | 0 |  |
| and | B... | B | Sind alle B... wahr? | 0 |  |
| or | B... | B | Ist eines der B... wahr? | 0 |  |
| not | B | B | Ist B falsch? | 0 |  |
| assert | B | B(true) / Er | Wenn Bi wahr ist, wird wahr ausgegeben, andernfalls ein Fehler | 1 |  |
| is-identical | E... | B | Sind alle E... identisch? | 0 |  |
| Listenfunktionen |  |  |  |  |  |
| get     | L N | E | Ntes Element in L | 0 |  |
| length  | L | N | Anzahl der Elemente von L | 0 |  |
| add     | L [N] E | L | Erweitert L um E [an Stelle N]  | 0 |  |
| set     | L N E | L | Setzt in L an Stelle N E ein | 0 |  |
| remove  | L N | L | Entfernt N-tes Element in L | 0 |  |
| sublist | L N1 N2 | L | Liste der Elemente N1 bis N2 von Li | 0 |  |
| random  | L | E | Zufälliges Element von L | 0 |  |
| range   | [N1]:0 N2 {[N3]:1 != 0} | L | Eine Liste aller Zahlen in N3-er Schritten von N1 bis N2. Ist N3 negativ, so wird heruntergezählt | 1 |  |
| append  | L... | L | Verbindet alle L... von links nach rechts zu einer Liste. Ist L... leer, so wird eine leere Liste ausgegeben | 1 |  |
| sort    | L F(a b) | L | Sortiert L durch Anwendung von F. F ist eine Funktion, die entweder -1 0 oder 1 ausgibt, abh&auml;ngig davon, ob a gleich/größer/kleiner b ist | 1 |  |
| shuffle | L | L | Mischt L zufällig | 1 |  |
| reverse | L | L | L rückwärts | 1 |  |
| flatten | L | L | Li wird als Baumstruktur angesehen. Lo sind dann die Blätter derselben | 1 |  |
| keep | L F(value=E index=N list=L)->B | L | Liste aller Elemente von Li, für die F true ist | 1 |  |
| find | L F(value=E index=N list=L)->B | N | Index des ersten Elementes von Li, für das F true ist. Existiert ein solches nicht, wird -1 returnt | 0, 1 |  |
| Zeichenketten |  |  |  |  |  |
| string-to-codepoints | S | L(N) | Liste aller Unicode-Codepoints von S | 1 |  |
| bytes-to-string-utf8 |  |  |  | 1 |  |
| codepoints-to-string | L(N) | S | Fügt alle Unicode-Codepoints zu einem S zusammen | 1 |  |
| to-uppercase | S | S | S in Großbuchstaben | 1 |  |
| to-lowercase | S | S | S in Kleinbuchstaben | 1 |  |
| chars | S | L(S) | Liste aller Zeichen von S | 0, 1 |  |
| value-string |  |  |  | 1 |  |
| code-string |  |  |  | 1 |  |
| join | E... | S | Wandelt jedes von E... in ein S um und verbindet diese | 1 |  |
| joinl |  |  |  | 1 |  |
| split | S1 S2 | L(S) | Spaltet S1 an jedem S2 | 0 |  |
| regex | S1 S2 S3 | S | Ersetzt in S3 jeden Text, für den S1 zutrifft, durch S2 | 0 |  |

## [Beispiele](./beispiele.md)

## Definiertes, implementationsdefiniertes und undefiniertes Verhalten

### Ungewöhnliches Verhalten

1. is-identical sagt aus, ob alle Operanden denselben Speicherbereich teilen und nicht, ob diese in Bezug auf alle Merkmale gleich sind. (Dafür gibt es `=`)
2. `{(def i 1) (def i 2) (+ 1 1)}` hat das Ergebnis `4`. Dies liegt daran, dass `i` auf `1` gesetzt wird, und im nächsten `def` daher `(def 1 2)` steht, sodass die Zahl `1` mit dem Wert `2` überschrieben wird. Der zurückzugebende Ausdruck ist dann nicht `(+ 1 1)`, sondern `(+ 2 2)`, der zu `4` evaluiert.
Daher ist es wichtig, wenn man eine Variable selbst und nicht ihren Wert meint, diese mit einem Apostroph zu quotieren.
`{(def 'i 1) (def 'i 2) (+ 1 1)}` hat das erwüschte Verhalten.

### Definiertes Verhalten

1. Der Interpreter darf davon ausgehen, dass ein Skript ausschließlich (implementations-)definiertes Verhalten aufweist.
2. Die Evaluation von Listenelementen mit Seiteneffekten erfolgt von links nach rechts, sofern nicht im Speziellen anders definiert. Diese Regel findet ebenfalls Anwendung auf jeden Ausdruck, der als Liste mit äquivalenter Funktion dargestellt werden kann.
3. Es werden im Sinne der Lazy-Evaluation nur die Argumente evaluiert, die verwendet werden.
4. Eine Division durch 0 erzeugt einen Fehler.
5. Erfüllt ein Ausdruck nicht die Bedingungen der Grammatik, wird ein Fehler erzeugt.
6. Erhält eine Funktion weniger Argumente als erwartet, so sind diese null.

### Implementationsabhängiges Verhalten

1. Datentypbeschränkungen der Doppelpunkt-Kurzschreibweise sind der oben aufgeführten Langformentsprechung vorzuziehen, da es implementationsabhängig ist, ob die entsprechende Langform als Datentypbeschränkung und nicht als allgemeine Beschränkung erkannt wird, für welche die Datentyp-Spezialisierungsregel nicht angewendet werden kann. Für die Doppelpunkt-Kurzschreibweise wird dagegen das beschriebene Verhalten garantiert.
2. Fehlermeldungen und ihre Darstellungsform sind implementationsdefiniert.
3. Die textuelle Darstellung von primitiven Datentypen ist implementationsdefiniert.
4. Man sollte sich nicht auf Identitäten bei primitiven Datentypen verlassen.
5. Funktionen mit leerer Parameterliste können sich entweder so verhalten, als sei `@void` implizit in dieser vorhanden oder als würde `@args` implizit in dieser vorhanden sein, ohne dass `@args` im Funktionsbauch definiert ist. (In diesem Falle würden überschüssige Argumente im Funktionsaufruf ignoriert werden.)
Eine Implementation muss sich für eines der beiden Verhalten entscheiden.
6. Wenn in einer Map eine Liste verschiedener Maps dem Schlüssel `@extends` zugeordnet ist und diese Maps dem gleichen Schlüssel einen unterschiedlichen Wert zuweisen, so
ist es implementationsabhängig, ob der Wert des letzten oder des ersten Auftretens genommen wird.

### Undefiniertes Verhalten

1. Das Verhalten des Interpreters bei undefiniertem Verhalten ist undefiniert.
2. Das Verhalten bei zu hoher Rekursionstiefe bzw. zu vielen verschachtelten Elementen ist undefiniert.
3. Es ist undefiniert, ob Ausdrücke ausgewertet werden, die das "Endergebnis" nicht beeinflussen.
4. Das Verhalten bei Umdefinierung der Grundfunktionen ist undefiniert.
5. Das Verhalten der sorted-Funktion, falls F einen Wert ausgibt, der nicht 0, 1, oder -1 ist.

## Glossar

- Objekt: Eine Map, die dazu dient, ein Objekt darzustellen
- Parameter: Von einer Methode erwarteter Wert
- Argument: An eine Methode übergebener Wert
- Methode: Ausdrücke, die Argumente entgegennehmen
- Funktion: Eine oder mehrere Methoden
- Überladen: Einer Funktion eine Methode hinzufügen
- Überschreiben: Den Wert eines Ausdrucks überschreiben

## Stilempfehlungen für Skriptdateien (unvollständig)

- Der Beginn aller Funktionsdefinitionen höchsten Levels wird nicht eingerückt.
- Schließende Klammern folgen niemals auf eine neue Zeile.
- Die Einrückung nimmt bei jeder Stufe um zwei Leerzeichen zu.
- Für robusten Code ist so weit wie möglich die reine Listendarstellung (etwa von Funktionsdefinitionen) zu verwenden, um Parsing-Fehler nicht zu provozieren.

## Bibliotheken

- STP-Skriptdateien, die neue Funktionen bereitstellen und mit einem include eingebunden werden, werden als Bibliotheken bezeichnet.
- Die corelib.stp-Bibliothek stellt gemeinsam mit der Kernimplementation dem Nutzer alle hier spezifizierten Grundfunktionen von STP zur Verfügung.
- Die stdmath.stp-Bibliothek erweitert STP um praktische Mathefunktionen.
- Die stdlib.stp-Bibliothek erweitert STP um praktische Funktionen. Sie enthält corelib.stp und stdmath.stp

## Inspirationsquellen

- [Scheme](http://www.scheme-reports.org/)
- [JavaScript](https://www.ecma-international.org/publications-and-standards/standards/ecma-262/)
- [Kotlin](https://kotlinlang.org/)
- [Snap!](https://snap.berkeley.edu/)
- [GP](https://gpblocks.org/)
- [Lua](https://www.lua.org/)

(c) Teutim 2022
