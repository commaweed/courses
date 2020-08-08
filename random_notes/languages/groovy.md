# Examples of things that are different in Groovy (versus Java)
## general notes

* everything in Groovy is an object, including operators (and their are no primitives)
* with asserts == represents equality (e.g. this.equals(that)), not identity (i.e ref == ref in java)
* with immutable objects, == is overloaded to represent equality, not identity
* Groovy uses dynamic method dispatch (Groovy types are not dynamic, they never change)
	* Method calls are funneled through an object called MetaClass
	* getMetaClass().invokeMethod(this, “foo”, EMPTY_PARAMS_ARRAY)
	* DefaultTypeTransformation.castToType (lookup this for list of all groovy casting logic)
* rule of thumb: As soon as you think about the type of a reference, declare it; if you’re thinking of it as “just an object,” leave the type out.
* you can override some operators

## Numbers
### numeric literals in Groovy

* java.lang.Integer 15, 0x1234ffff
* java.lang.Long 100L, 200l ← lower-case LinkedHashMap
* java.lang.Float 1.23F, 1.23F
* java.lang.Double 1.23D, 1.23d
* java.math.BigInteger 123g, 456G
* java.math.BigDecimal 1.23, 1.4E4, 1.23g, 4.56G
* for operations +, -, and multiplication:
	* If either operand is a Float / Double, result is a Double
	* Otherwise, if either operand is a BigDecimal, result is a BigDecimal
	* Otherwise, if either operand is a BigInteger, the result is a BigInteger
	* Otherwise, if either operand is a Long, the result is a Long
	* Otherwise, the result is an Integer
* for division
	* if either operand is a Float / Double, result is a Double
	* otherwise, the result is a BigDecimal with max precision of both args, rounded half-up (normalized - no trailing zeros)
	* Integer division is achievable through explicit casting or by using intdiv()
	* shifting operators are bit-shifts only for Integer and Long (you implement them for other types through op overloading)
	* the power operator coerces to the next best type that can handle the result in terms of range (Integer, Long, then Double)
	* the equals operator coerces to the more general type before comparing

| Expression Result | Type | Comments | 
| ---- | ---- | ---- |
| 1/2 | BigDecimal (0.5) | In Java, the result would be the integer 0 | 
| (int)(1/2) | Integer (0) | This is normal coercion of BigDecimal to Integer. | 
| 1.intdiv(2) | Integer (0) | This is the equivalent of the Java 1/2. | 
| Integer.MAX_VALUE+1 | Integer | Non-power operators wrap without promoting the result type. | 
| 2**30 | Integer  |  | 	
| 2**31 | BigInteger | The power operator promotes where necessary. | 
| 2**3.5 | Double |  |  	
| 2G+1G | BigInteger  |  | 	
| 2.5G+1G | BigDecimal |  |  	
| 1.5G==1.5F | Boolean (true) | Float is promoted to BigDecimal before comparison. | 
| 1.1G==1.1F | Boolean (false) | 1.1 can’t be exactly represented as a Float or Double, so when it’s promoted to BigDecimal, it isn’t equal to the exact BigDecimal 1.1G but rather 1.100000023841858G. | 
```groovy
assert 1 == (-1).abs()
assert 2 == 2.5.toInteger() // conversion
assert 2 == 2.5 as Integer // enforced coercion
assert 2 == (int) 2.5 // cast
assert 3 == 2.5f.round()
assert 3.142 == Math.PI.round(3)
assert 4 == 4.5f.trunc()
assert 2.718 == Math.E.trunc(3)
assert '2.718'.isNumber() // String methods
assert 5 == '5'.toInteger()
assert 5 == '5' as Integer
assert 53 == (int) '5' // (asci 53 = '5')
assert '6 times' == 6 + ' times' // Number + String
```
### some GDK methods on numbers
```groovy
def store = ''
10.times{ store += 'x' }
assert store == 'xxxxxxxxxx'

store = ''
1.upto(5) { number -> store += number }
assert store == '12345'

store = ''
2.downto(-2) { number -> store += number + ' ' }
assert store == '2 1 0 -1 -2 '

store = ''
0.step(0.5, 0.1){ number -> store += number + ' ' }
assert store == '0 0.1 0.2 0.3 0.4 '
```
### Decimals can use exponents, with the e or E exponent letter
```groovy
assert 1e3  ==  1_000.0        // 1e3 == 1 x 10^3 (scientific notation)
assert 2E4  == 20_000.0
assert 3e+1 ==     30.0
assert 4E-2 ==      0.04    
assert 5e-1 ==      0.5        // 5e-1 == 5 x 10^-1
```
### Arithmetic operators also work with BigInteger and BigDecimal
```groovy
def one = new BigInteger('456')
def two = new BigDecimal('123.45') 
assert (one + two) == 579.45d //the d denotes a BigDecimal literal, but its optional
assert (one + two) instanceof java.math.BigDecimal
```
### Division
```groovy
// division produces double if operand float or double, and BigDecimal otherwise
assert (1.0f / 0.2f) instanceof java.lang.Double
assert (5L / 2i) instanceof java.math.BigDecimal

// so how do we get integral type from integral division (java truncates)
assert (5L).intdiv(2i) instanceof java.lang.Long
```
### The power operator (in java Math.pow(x,y))
```groovy
assert    2 ** 3    instanceof Integer        //  2^3 = 8
assert   10 ** 9 == 1_000_000_000

// the result can't be represented as an Integer or Long, so return a BigInteger
assert  100 ** 10    instanceof BigInteger 
assert  100 ** 10 == 1e20                     // 1 x 10^10
```
### some groovy examples of how numbers are treated as objects
```groovy
(60 * 60 * 24 * 365).toString(); // invalid Java
int secondsPerYear = 60 * 60 * 24 * 365; secondsPerYear.toString(); // invalid Java
new Integer(secondsPerYear).toString();
assert "abc" - "a" == "bc" // invalid Java
assert 'ABCDE'.indexOf(67) == 2
```
### assigning types to numbers doesn’t imply they will have that type
```groovy
int c = 1 // explicit type becomes java.lang.Integer (because Groovy doesn't use primitives)
float d = 1    // java.lang.Float
```
### coercion of numbers (promoting to more general type)
```groovy
1 + 1.0 // add Integer to BigDecimal - need to promote Integer (more specific type) to BigDecimal (more general type)
```
## Boolean
### Groovy Truth
| Runtime type | Evaluation criterion required for truth |
| ---- | ---- |
| Boolean | Corresponding Boolean value is true |
| Matcher | Matcher has a match |
| Collection Collection | is nonempty |
| Map Map | is nonempty |
| String, GString | String is nonempty |
| Number, Character | Value is nonzero |
| None of the above | Object reference is non-null |
```groovy
//Groovy has special rules for coercing non-boolean objects to a boolean value 
// True if the Matcher has at least one match.
assert ('a' =~ /a/)
assert !('a' =~ /b/)

// Iterators and Enumerations with further elements are coerced to true.
assert [0].iterator()
assert ![].iterator()

// Non-empty Maps are evaluated to true.
assert ['one' : 1]
assert ![:]

// Non-empty Strings, GStrings and CharSequences are coerced to true.
assert 'a'
assert !''
def nonEmpty = 'a'
assert "$nonEmpty"
def empty = ''
assert !"$empty"

// Non-zero numbers are true.
assert 1
assert 3.5
assert !0

// Non-null object references are coerced to true.
assert new Object()
assert !null

// Customizing the truth with asBoolean() methods
// In order to customize whether groovy evaluates your object to true or false implement asBoolean()
class Color {
    String name

    boolean asBoolean(){
        name == 'green' ? true : false
    }
}
assert new Color(name: 'green')
assert !new Color(name: 'red')
```
### watch out for groovy truth coding errors
```groovy    
// normal comparison
def x = 1
if (x == 2) {
	assert false
}

// compile error - top level if doesn’t allow assignment
/*
if (x = 2) {
println x
}
**/

// 1. ouch! nested expression is ok; assignment occurs and then groovy thruth evaluation occurs
if ((x = 3)) { println x }
assert x == 3

// deliberate assign and test in a while statement is ok (it will loop until x is 0)
def store = []
while (x = x - 1) { store << x }
assert store == [2, 1]

// 2. ouch! this will print 2
while (x = 2) {
println x
break
}
```

### groovy supports a shortcut for ? : 

```groovy
def argument = "given"
def standard = "default"
def result = argument ? argument : standard
def value = argument ?: standard    // you can rewrite the above using short notation

assert result == "given"
assert value == "given"
assert result == value
```
### iscase / equals / isInstance / matcher

* A classifier is eligible as a switch case if it implements the isCase method
* Unlike Java’s constant cases, the candidate may match more than one classifier; implies order of cases is important.
* standard implementations of isCase for switch, grep, and in

| Class | a.isCase(b) implemented as | 
| ---- |:----:|
| Object | a.equals(b) | 
| Class | a.isInstance(b) | 
| Collection | a.contains(b) | 
| Range | a.contains(b) | 
| Pattern | a.matcher(b.toString()).matches() | 
| String | (a==null && b==null) or a.equals(b) | 
| Closure | a.call(b) | 

* The isCase method is also used with grep on collections such that collection.grep(classifier) returns a collection of all items that are a case of that classifier.

```groovy
switch (10) {
    case 0 : assert false ; break
    case 0..9 : assert false ; break
    case [8,9,11] : assert false ; break
    case Float : assert false ; break // we classify by type (GDK enhances Class by adding an isCase method - tests with isInstance)
    case {it%3 == 0}: assert false ; break
    case ~/../ : assert true ; break    // A pattern has an isCase method; applies test to the toString of the argument
    default : assert false ; break
}

// example in operator
def okValues = [1, 2, 3]
def value = 2
assert value in okValues

// assertions can be instrumented with a trailing message
input = new File('no such file')
assert input.exists() , "cannot find '$input.name'"    //... cannot find 'no such file'. Expression: input.exists()
assert input.canRead() , "cannot read '$input.canonicalPath'"
println input.text
```

### loops

* while loops can use groovy truths (i.e. boolean test expressions) - the Boolean test is evaluated, and if it’s true, the body of the loop is then executed.
* there are no do {} while(condition) or repeat {} until (condition) loops in Groovy.
* for (variable in iterable) { body } // variable can have an optional type
* the body of a for loop is not a closure

```groovy
// some while loop examples
def list = [1,2,3]
while (list) { list.remove(0) }
assert list == []
while (list.size() < 3) list << list.size()+1
assert list == [1,2,3]

// for: explicit typing over strict range
def store = ''
for (String s in 'a'..'c') store += s
assert store == 'abc'

// for: explicit typing over list as collection
store = ''
for (i in [1, 2, 3]) { store += i }
assert store == '123'

// for: explicit typing using java-style for loop
def myString = 'Old school Java'
store = ''
for (int i=0; i < myString.size(); i++) { store += myString[i] }
assert store == myString

// for: explicit typing using java-style for iterable index
myString = 'Java range index'
store = ''
for (int i : 0 ..< myString.size()) { store += myString[i] }
assert store == myString

// for: implicit typing over half-exclusive IntRange
myString = 'Groovy range index'
store = ''
for (i in 0 ..< myString.size()) { store += myString[i] }
assert store == myString

// for: java-style for-in
myString = 'Java string Iterable'
store = ''
for (String s : myString) { store += s }
assert store == myString

// for: groovy-style for-in
myString = 'Groovy iterator'
store = ''
for (s in myString) { store += s }
assert store == myString

// If the container object is null, no iteration will occur
for (x in null) println 'This will not be printed!'    

// If Groovy cannot make the container object iterable by any means, the fallback solution is to do an iteration that contains only the container object itself
for (x in new Object()) println "Printed once for object $x" 

// for loop body is not a closure
for (x in 0..9) { println x }

// but the body of each is a closure
(0..9).each { println it }
```

## Strings
### Quoted identifiers (and string types)

```groovy
def map = [:]
map.'single quote'                //java.lang.String (no interpolation support)
map."double quote"                //interpolated java.lang.String if no interpolation, groovy.lang.GString otherwise 
map.'''triple single quote'''    //multiline java.lang.String (no interpolation support)
map."""triple double quote"""    //multiline/interpolated java.lang.String no interpolation, groovy.lang.GString otherwise 
map./slashy string/                //multiline/interpolated good for regular expressions (no need to escape the \, but / does)
map.$/dollar slashy string/$    //multiline/iterpolated ($ is the escape (\ is escape for the others))

/* note: multiline strings preserves whitespace if indenting.  But, The Groovy Development Kit 
contains methods for stripping out the indentation with the String#stripIndent() method, and 
with the String#stripMargin() method that takes a delimiter character to identify the text 
to remove from the beginning of a string. Also can use \ to exclude carriage return as first 
character*/

// Neither double quotes nor single quotes need be escaped in triple double quoted strings.
```

### GString interpolation (resolving values)

```groovy
/* Any Groovy expression can be interpolated in all string literals (sep ' types).  Not only 
expressions are actually allowed in between the ${} placeholder. Statements are also allowed,
but a statement’s value is just null. So if several statements are inserted in that placeholder,
the last one should somehow return a meaningful value to be inserted.  
Example "The sum of 1 and 2 is equal to ${def a = 1; def b = 2; a + b}"
In addition to ${} placeholders, we can also use a lone $ sign prefixing a dotted expression (a.b.c)
*/

def firstname = "Homer"
def map = [:]
map."Simson-${firstname}" = "Homer Simson"
assert map.'Simson-Homer' == "Homer Simson"
println "hello $firstname or ${firstname.toString()}"
```

### GString interpolation using closures - lazy evaluation

```groovy
//When the placeholder contains an arrow, ${→}, the expression is actually a closure expression.

// 1. parameterless closure which doesn’t take arguments
def sParameterLessClosure = "1 + 2 == ${-> 3}" 
assert sParameterLessClosure == '1 + 2 == 3'    

// 2. closure takes a single java.io.StringWriter argument, to which you can append content with the <<
def sOneParamClosure = "1 + 2 == ${ w -> w << 3}" 
assert sOneParamClosure == '1 + 2 == 3'

/* In appearance, it looks like a more verbose way of defining expressions to be interpolated, but closures
 have an interesting advantage over mere expressions: lazy evaluation. */
def number = 1 
def eagerGString = "value == ${number}"
def lazyGString = "value == ${ -> number }"
assert eagerGString == "value == 1" 
assert lazyGString ==  "value == 1" 
number = 2 
assert eagerGString == "value == 1" 
assert lazyGString ==  "value == 2" //closure uses new value

/* An embedded closure expression taking more than one parameter will generate an exception at runtime.
 Only closures with zero or one parameters are allowed. */
```

### GString versus String (they are not the same, although they could appear to be the same)

```groovy
// String is immutable and will have same hashcode when copared to self, GString is note
println "one: ${1}"; println "one: 1"
assert "one: ${1}".hashCode() != "one: 1".hashCode()
output:
one: 1
one: 1

// using GString as Map keys should be avoided
def key = "a"
def m = ["${key}": "letter ${key}"]     
assert m["a"] == null
```

### GString placeholder evaluation

* Each placeholder inside a GString is evaluated at declaration time; result is stored in a GString object
* By the time the GString value is converted to a java.lang.String (toString() or casting), each value gets written to the string.

### Unlike Java, Groovy doesn’t have an explicit character literal; ways to convert it to character

```groovy
// 1. create java static type
char c1 = 'A' 
assert c1 instanceof Character

// 2. use 'as' keyword (type coercion)
def c2 = 'B' as char 
assert c2 instanceof Character

// 3. use static type cast
def c3 = (char) 'C' 
assert c3 instanceof Character

// 4. use method
'A'.toCharacter()
```

### some miscellaneous string examples

```groovy
String greeting = 'Hello Groovy!'
assert greeting.startsWith('Hello')
assert greeting.getAt(0) == 'H'
assert greeting[0] == 'H'
assert greeting.indexOf('Groovy') >= 0
assert greeting.contains('Groovy')
assert greeting[6..11] == 'Groovy'
assert 'Hi' + greeting - 'Hello' == 'Hi Groovy!'
assert greeting.count('o') == 3
assert 'x'.padLeft(3) == ' x'
assert 'x'.padRight(3,'_') == 'x__'
assert 'x'.center(3) == ' x '
assert 'x' * 3 == 'xxx'

def greeting = 'Hello'
greeting <<= ' Groovy' // #1 Leftshift and assign
assert greeting instanceof java.lang.StringBuffer
greeting << '!' //#2 Leftshift on StringBuffer
assert greeting.toString() == 'Hello Groovy!'
greeting[1..4] = 'i' //#3 Substring 'ello' becomes 'i'
assert greeting.toString() == 'Hi Groovy!'

using a left shift operator on String returns a StringBuilder

def greeting = 'Hello'
assert greeting instanceof java.lang.String
greeting <<= ' Groovy' // #1 Leftshift and assign
assert greeting instanceof java.lang.StringBuffer
greeting << '!' //#2 Leftshift on StringBuffer
assert greeting.toString() == 'Hello Groovy!'
greeting[1..4] = 'i' //#3 Substring 'ello' becomes 'i'
assert greeting.toString() == 'Hi Groovy!'​
```

### some properties with GString

```groovy
def me = 'Tarzan'
def you = 'Jane'
def line = "me $me - you $you"
assert line == 'me Tarzan - you Jane'
assert line instanceof GString
assert line.strings[0] == 'me '
assert line.strings[1] == ' - you '
assert line.values[0] == 'Tarzan'
assert line.values[1] == 'Jane' 14
```

### regular expressions

* regex find operator, =~ (its a matcher, but can also be used as a Boolean conditional)
* regex match operator, ==~ (more restrictive because the match must cover the entire string)
* regex pattern operator, ~string
* Sometimes the slashy syntax interferes with other valid Groovy expressions
	* such as line comments or numerical expressions with multiple slashes for division
	* put parentheses around your pattern like (/pattern/)
	* Parentheses force the parser to interpret the content as an expression
* groovy supports the following tasks for RE:
	* Tell whether the pattern fully matches the whole string.
	* Tell whether there’s an occurrence of the pattern in the string.
	* Count the occurrences.
	* Do something with each occurrence.
	* Replace all occurrences with some text.
	* Split the string into multiple strings by cutting at each occurrence.
* two different ways to iterate through matches with identical behavior:
	* use String.eachMatch(Pattern)
	* use Matcher.each()

```groovy
def twister = 'she sells sea shells at the sea shore of seychelles'

// twister must contain a substring of size 3 that starts with s and ends with a
assert twister =~ /s.a/

def finder = (twister =~ /s.a/)
assert finder instanceof java.util.regex.Matcher 
println finder.find() // true
println finder.group() // sea

// twister must contain only words delimited by single spaces
assert twister ==~ /(\w+ \w+)*/

def WORD = /\w+/
matches = (twister ==~ /($WORD $WORD)*/)
assert matches instanceof java.lang.Boolean

assert !(twister ==~ /s.e/)

def wordsByX = twister.replaceAll(WORD, 'x')
assert wordsByX == 'x x x x x x x x x x'

def words = twister.split(/ /)
assert words.size() == 10
assert words[0] == 'she'

def myFairStringy = 'The rain in Spain stays mainly in the plain!'

// words that end with 'ain': \b\w*ain\b
def wordEnding = /\w*ain/
def rhyme = /\b$wordEnding\b/
def found = ''

myFairStringy.eachMatch(rhyme) { match ->
   found += match + ' '
}

assert found == 'rain Spain plain '

found = ''
(myFairStringy =~ rhyme).each { match ->
    found += match + ' '
}
assert found == 'rain Spain plain '

def cloze = myFairStringy.replaceAll(rhyme){ it-'ain'+'___' }
assert cloze == 'The r___ in Sp___ stays mainly in the pl___!'

def matcher = 'a b c' =~ /\S/
assert matcher[0] == 'a'
assert matcher[1..2] == ['b','c']
assert matcher.size() == 3

// Groovy parallel assignment example
def (a,b,c) = 'a b c' =~ /\S/
assert a == 'a'
assert b == 'b'
assert c == 'c'

// what matcher[0] returns depends on whether the pattern contains groupings
def matcher = 'a:1 b:2 c:3' =~ /(\S+):(\S+)/
assert matcher.hasGroup()
assert matcher[0] == ['a:1', 'a', '1'] // 1st match
assert matcher[1][2] == '2' // 2nd match, 2nd group

// the matcher will match 3 times, passing full match and the two groups into each closure
// match[1]=key and match[2]=value
def matcher = 'a:1 b:2 c:3' =~ /(\S+):(\S+)/
matcher.each { full, key, value ->
assert full.size() == 3 // a:1, b:2, c:3
assert key.size() == 1 // a, b, c
assert value.size() == 1 // 1, 2, 3
}
```

## ranges

* left..right
* (left..right) - it has a low precedence so you may need to include parenthesis
* (left..<right) - half-exclusive range - value on the right isn’t part of the range
* You can switch lower side with higher side for a reverse range
* a range can have bounds other than integers, such as dates and strings
* groovy supports ranges at the language level with a special for-in-range loop
* Date objects can be used in ranges c because the GDK adds the previous and next methods to date, which increases or decreases the date by one day.
* The GDK also adds minus and plus operators to java.util.Date, which increases or decreases the date by so many days.
* The String methods previous and next are added by the GDK to make strings usable for ranges
* The last character in the string is incremented or decremented, and overflow or underflow is handled by appending a new character or deleting the last character

```groovy
// inclusive ranges
assert (0..10).contains(0)
assert (0..10).contains(5)
assert (0..10).contains(10)
assert (0..10).contains(-1) == false
assert (0..10).contains(11) == false

// half-exclusive ranges
assert (0..<10).contains(9)
assert (0..<10).contains(10) == false

// references to ranges
def a = 0..10
assert a instanceof Range // groovy.lang.Range
assert a.contains(5)

// explicit construction
a = new IntRange(0,10)
assert a.contains(5)

// bounds checking
assert (0.0..1.0).contains(1.0)
assert (0.0..1.0).containsWithinBounds(0.5)

// date ranges
def today = new Date()
def yesterday = today - 1
assert (yesterday..today).size() == 2

// string ranges
assert ('a'..'c').contains('b')

// for-in-range loop
def log = ''
for (element in 5..9) {
    log += element
}
assert log == '56789'

// loop with reverse range
log = ''
for (element in 9..5){
    log += element
}
assert log == '98765'

// half-exclusive, reverse, each with closure (also shows iteration method each())
log = ''
(9..<5).each { element ->
    log += element
}
assert log == '9876'

assert 5 in 0..10
assert (0..10).isCase(5)

// ranges for classification
def age = 36
switch(age){
    case 16..20 : insuranceRate = 0.05 ; break
    case 21..50 : insuranceRate = 0.06 ; break
    case 51..65 : insuranceRate = 0.07 ; break
    default: throw new IllegalArgumentException()
}
assert insuranceRate == 0.06

// filtering with ranges
def ages = [20, 36, 42, 56]
def midage = 21..50
assert ages.grep(midage) == [36, 42]
```

* Any data type can be used with ranges as long as:
	* it implements next and previous; that is, it overrides the ++ and — operators.
	* it implements java.lang.Comparable; that is, it implements compareTo, effectively overriding the <=> (spaceship) operator

```groovy
class Weekday implements Comparable {
    static final DAYS = [
        'Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'
    ]

    private int index = 0


    Weekday(String day) { index = DAYS.indexOf(day) } // allow all values
    Weekday next() { return new Weekday(DAYS[(index + 1) % DAYS.size()]) }
    Weekday previous() { return new Weekday(DAYS[index - 1]) } // range bound methods
    int compareTo(Object other) { return this.index <=> other.index }
    String toString() { return DAYS[index] }
}

def mon = new Weekday('Mon')
def fri = new Weekday('Fri')
def worklog = ''
for (day in mon..fri) {
    worklog += day.toString() + ' '
}
assert worklog == 'Mon Tue Wed Thu Fri '
```

### Lists and arrays Arrays

* Groovy uses a comma-separated lists of values, surrounded by square brackets, for list and arrays
* lists make use of the subscript operator to retrieve and assign values, but they are bound to integer types
* Lists are by default of type java.util.ArrayList
* Lists can be created and initialized at the same time by calling toList on ranges
* The GDK extends all arrays, collection objects, and strings with a toList method that returns a newly generated list of the contained elements. Strings are handled like lists of characters.
* lists implement the getAt and putAt methods to support the subscript operator.
* there are also operators available to change the contents of the list in a more drastic way:
	* plus(Object)
	* plus(Collection)
	* leftShift(Object)
	* minus(Collection)
	* multiply
* the equals method on lists tests that two collections have equal elements (java.util.List#equals)
* some list methods not covered: collate, collectMany, combinations, dropWhile, flatten, groupBy, permutations, take, transpose, and withIndex.
* The GDK also introduces two convenience methods for producing views backed by an existing list: asImmutable and asSynchronized.
	* These methods use Java’s Collections.unmodifiableList and Collections.synchronizedList

### Lists

```groovy
// homogeneous list
def numbers = [1, 2, 3]
assert numbers.size() == 3
assert numbers[0] == 1
assert numbers instanceof java.util.List
assert numbers instanceof ArrayList

// heterogeneous list
def manyTypes = [ 1, "a", true ]
assert manyTypes instanceof java.util.ArrayList

// empty list
List emptyList = []
assert emptyList.size() == 0

// long list
List longList = (0..1000).toList()
assert longList[555] == 555

// coerce to a different type of List
def coercionOkay = [ 1, 2, 3 ] as LinkedList

// access elements using array-like [#] notation
def letters = ['a', 'b', 'c', 'd']
assert letters[0] == 'a'
assert letters[-1] == 'd'
println letters[4]  // displays null (no ArrayIndexOutOfBoundsException)
//println letters[-5] //ArrayIndexOutOfBoundsException

// modify list element
letters[2] = 'C'
assert letters[2] == 'C'

// add to the end of the list
letters << 'e'
assert letters[4] == "e"

// return a new list 
def newList = letters[1, 3]
assert newList == ['b', "d"]
assert newList instanceof java.util.ArrayList
assert letters[2..4] == ['C', 'd', 'e']     // using range to make new list

// list of lists
def multi = [[0, 1], [2, 3]]     
assert multi[1][0] == 2 

myList = ['a','b','c','d','e','f']
assert myList[0..2] == ['a','b','c']        // getAt(range)
assert myList[0,2,4] == ['a','c','e']        // getAt(collection of indexes)

myList[0..2] = ['x','y','z']                // putAt(Range)
assert myList == ['x','y','z','d','e','f']    

myList[3..5] = []                            // removing elements
assert myList == ['x','y','z']

myList[1..1] = [0, 1, 2]                    // adding elements
assert myList == ['x', 0, 1, 2, 'z']

// plus operator
myList = []
myList += 'a'                                 // plus(Object)            
assert myList == ['a']    
myList += ['b','c']                            // plus(Collection)            
assert myList == ['a','b','c']

// some other operators
myList = []
myList << 'a' << 'b'                        // left shift operator (like append)
assert myList == ['a','b']
assert myList - ['b'] == ['a']                 // minus(collection)
assert myList * 2 == ['a','b','a','b']        // multiply

// control structures - can be used for execution of flow (if, switch, and for control structures)
myList = ['a', 'b', 'c']
assert myList.isCase('a')
assert 'b' in myList

def candidate = 'c'
switch(candidate){
    case myList : 
        assert true; break                    // contains
    default : 
        assert false
}

assert ['x','a','z'].grep(myList) == ['a']    // intersection filter

myList = []
if (myList) assert false                    // groovy truth - empty lists are false

// Lists can be iterated with a 'for' loop
def expr = ''
for (i in [1,'*',5]){
    expr += i
}
assert expr == '1*5'

// API list method examples
//==========================
assert [1,[2,3]].flatten() == [1,2,3]
assert [1,2,3].intersect([4,3,1])== [3,1]
assert [1,2,3].disjoint([4,5,6])

// treating a list like a stack
list = [1,2,3]
popped = list.pop()
assert popped == 3
assert list == [1,2]
list.push(3)
assert list == [1,2,3]

assert [1,2].reverse() == [2,1]
assert [3,1,2].sort() == [1,2,3]

// comparing lists by first element
def list = [ [1,0], [0,1,2] ]
list = list.sort { a,b -> a[0] <=> b[0] } // sorting by first element value
assert list == [ [0,1,2], [1,0] ]

// comparing lists by size
list = list.sort { item -> item.size() }    // sorting by size
assert list == [ [1,0], [0,1,2] ]

// removing by index
list = ['a','b','c']
list.remove(2)
assert list == ['a','b']
list.remove('b')
assert list == ['a']

// removing by value
list = ['a','b','b','c']
list.removeAll(['b','c'])
assert list == ['a']

// transform one list into another (similar to map() in java 8)
def doubled = [1,2,3].collect{ item -> item * 2 }
assert doubled == [2,4,6]

// finding every element matching the closure
def odd = [1,2,3].findAll{ item -> item % 2 == 1 }
assert odd == [1,3]

// two ways to remove duplicates 
def x = [1, 1, 1]
assert [1] == new HashSet(x).toList()     // convert to set, then back to list (creates a new collection)
assert [1] == x.unique()                 // call unique to remove duplicates (does not create a new collection)

// two ways to remove null from a collection
def x = [1,null,1]
assert [1,1] == x.findAll{it != null}    // use a closure to match on non-null elements
assert [1,1] == x.grep{it}                // each element is subjected to groovy truth (null is false)

def list = [1, 2, 3, 4, 5, 3]
assert list.first() == 1
assert list.head() == 1
assert list.tail() == [2, 3, 4, 5, 3]
assert list.last() == 3
assert list.count(3) == 2
assert list.max() == 5
assert list.min() == 1
assert 2 = list.find { item -> item % 2 == 0 }     // find the first even number
assert list.every { item -> item < 5 }            // is every item less than 5
assert list.any { item -> item < 2 }            // is any item < 2

def store = ''
list.each { item -> store += item }
assert store == '123453'
store = ''
list.reverseEach { item -> store += item }
assert store == '354321'

def store = ''
list.eachWithIndex { item, index -> store += "$index:$item " }
assert store == '0:1 1:2 2:3 3:4 4:5 5:3 '
assert list.join('-') == '1-2-3-4-5-3'
result = list.inject(8) { clinks, guests -> clinks + guests }    // similar to reduce(sum) in java  
assert result == 8 + 1 + 2 + 3 + 4 + 5 + 3    // 26
assert list.sum() == 18
factorial = list.inject(7) { fac, item -> fac * item }    // similar to reduce(multiply) in java 
assert factorial == 7 * 1 * 2 * 3 * 4 * 5 * 3
```

* In addition to positive index values, lists can be subscripted with negative indexes that count from the end of the list backward
* Avoid negative indexes with half-exclusive ranges

| Example List Value | | | 0 | 1 | 2 | 3 | 4 | | |
| ---- |:----:|:----:|:----:|:----:|:----:|:----:|:----:|:----:|:----:|
| Positive Index  | | | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 	
| Negative Index | -7 | -6 | -5 | -4 | -3 | -2 | -1 |  | |  	
| Bounds (out or in) | O | O | I | I | I | I | I | O | O | 

```groovy
// the above table uses the following code
def list = [0,1,2,3,4]
def reverse = list[4..0] // creates a new list
assert reverse == [4,3,2,1,0]
assert list[-1] == 4 // last entry of non-empty list
assert list[-2] == 3
assert list[-3..-1]​ == [2,3,4]
assert reverse[-1] == 0
assert reverse[-2] == 1
assert reverse[-3..-1] == [2,1,0]
```

### Arrays need to be coerced or casted as such (otherwise they’ll be lists)

```groovy
// define array
String[] arrStr = ['Ananas', 'Banana', 'Kiwi']  
assert arrStr instanceof String[]    
assert !(arrStr instanceof List)

// coerce array
def coercedArray = ['Ananas', 'Banana', 'Kiwi'] as String[]
assert coercedArray instanceof String[] 

// create new array (example using multi-dimensional)
def matrix1 = new Integer[3][3]         
assert matrix1.size() == 3
assert matrix1[0][0] == null
Integer[][] matrix2
matrix2 = [[1,2],[3,4]]     //looks like list notation, but it's an array
assert matrix2 instanceof Integer[][]

// java array initializer is not supported for Groovy ({} confused as closure)
// int[] matrix3 = new int[] { 1, 2, 3 }    //produces compile error
```

### Maps (also called dictionaries or associative arrays)

* roovy uses a comma-separated list of values for maps, accept each value will be a key:value pair.
* ust like lists, maps make use of the subscript operator to retrieve and assign values but can use any arbitrary type
* iterally declared maps are of type LinkedHashMap (can rely on the ordering of entries, keys, and values)
* ap’s each method takes a closure that takes either a Map.Entry object or key / value pair depending on whether the closure takes one or two arguments

```groovy
// declare an empty map
def map = [:]
assert emptyMap.size() == 0

// map definition (the names become strings)
def colors = [red: '#FF0000', green: '#00FF00', blue: '#0000FF']
assert colors instanceof java.util.Map
assert colors instanceof java.util.HashMap
assert colors instanceof java.util.LinkedHashMap    // this is actual type
assert colors['red'] == '#FF0000'
assert colors.green  == '#00FF00'  
colors['pink'] = '#FF00FF'          // one way to put a key/value pair
colors.yellow  = '#FFFF00'          // another way to put
assert colors.size() == 5

// if it can determine the type, it won't make key a string type
def numbers = [ 1: 'one', 2: 'two' ]
assert numbers[1] == 'one'
assert numbers.keySet() instanceof Set
assert numbers.keySet()[0] == 1
assert numbers.keySet()[0] instanceof Integer
assert numbers.containsKey(1)

// do not use variable values as keys; you can get runtime error
def key = 'name'
def person = [ key: 'Guillame' ] // Groovy makes key[0] = 'key', not 'name'
assert !person.containsKey('name')
assert person.containsKey('key')
//quotes required on identifiers that are not valid (e.g. space, hyphen, etc.)
person."street-name" = 'fake street'
person./1xxx/ = /ksdjf/
assert person[/1xxx/] == /ksdjf/

// you can use variables as keys if you escape it with ()
person = [(key): 'Guillaume'] 
assert person.containsKey('name')    
assert !person.containsKey('key')

def myMap = [a:1, b:2, c:3]
def explicitMap = new TreeMap()
explicitMap.putAll(myMap)
assert explicitMap['a'] == 1
def composed = [x:'y', *:myMap]                // use of the spread operator
assert composed == [x:'y', a:1, b:2, c:3]

// strings around key that are of type String are optional (cannot contain special characters)
assert ['a':1] == [a:1]

// keys derived from local variables
def x = 'a'
assert ['x':1] == [x:1]
assert ['a':1] == [(x):1]

def myMap = [a:1, b:2, c:3]

// retrieve existing elements
assert myMap['a'] == 1
assert myMap.a == 1
assert myMap.get('a') == 1
assert myMap.get('a',0) == 1

// attempting to retrieve non-existing elements (produces null)
assert myMap['d'] == null
assert myMap.d == null
assert myMap.get('d') == null

// default value
println myMap                       // [a:1, b:2, c:3]
assert myMap.get('d',0) == 0        // inserts default value if not present
println myMap                       // [a:1, b:2, c:3, d:0]
assert myMap.d == 0            

// single put
myMap['d'] = 1                        // d was 0, now it is 1
assert myMap.d == 1
myMap.d = 2                            // d was 1, now it is 2
assert myMap.d == 2

// if key contains special characters 
myMap = ['a.b':1]
assert myMap.'a.b' == 1

def myMap = [a:1, b:2, c:3]
def other = [b:2, c:3, a:1]

// call to equals()
assert myMap == other

// JDK methods
assert !myMap.isEmpty()
assert myMap.size() == 3
assert myMap.containsKey('a')
assert myMap.containsValue(1)
assert myMap.entrySet() instanceof Collection

// GDK methods
assert myMap.any { entry -> entry.value > 2 }
assert myMap.every { entry -> entry.key < 'd' }
assert myMap.keySet() == ['a','b','c'] as Set
assert myMap.values().toList() == [1, 2, 3]

// iterating over maps

def myMap = [a:1, b:2, c:3]
def store = ''

myMap.each { entry ->
    store += entry.key
    store += entry.value
}
assert store == 'a1b2c3'

store = ''
myMap.each { key, value ->
    store += key
    store += value
}
assert store == 'a1b2c3'

store = ''
for (key in myMap.keySet()) { store += key }
assert store == 'abc'

store = ''
for (value in myMap.values()) { store += value }
assert store == '123'

// changing map content and building new objects from it

def myMap = [a:1, b:2, c:3]
myMap.clear()                // jdk
assert myMap.isEmpty()        // jdk

myMap = [a:1, b:2, c:3]
myMap.remove('a')
assert myMap.size() == 2

assert [a:1] + [b:2] == [a:1, b:2]

myMap = [a:1, b:2, c:3]
def abMap = myMap.subMap(['a', 'b'])
assert abMap.size() == 2

abMap = myMap.findAll { entry -> entry.value < 3 }
assert abMap.size() == 2
assert abMap.a == 1

def found = myMap.find { entry -> entry.value < 2 }    // find first value < 2
assert found.key == 'a'
assert found.value == 1

def doubled = myMap.collect { entry -> entry.value *= 2 }     // transform, like map() in java 8
assert doubled instanceof List
assert doubled.every { item -> item % 2 == 0 }

def addTo = []
myMap.collect(addTo) { entry -> entry.value *= 2 }
assert addTo instanceof List
assert addTo.every { item -> item % 2 == 0 }
```

### Closures

* Groovy provides a default name “it”, so that you don’t need to declare it specifically
* When there’s only one parameter passed into the closure, its declaration is optional. The magic variable “it” can be used instead.
* whenever you see braces of a closure, thing “new groovy.lang.Closure() {}”
* Closures are objects
	* they can be stored in variables
	* they can be passed around
	* you can call methods on them
* you can reference a method as a closure using the “reference.&” operator
	* def c = reference.&someMethod
* You can support closures with diffrerent parameter styles by using Closure’s getMaximumNumberOfParameters() and getParameterTypes()
* closure composition (f(g(x))) - pointing from the inner to the outer closure
* you can use the closure memoize() function to cache results; some other methods
	* memoizeAtMost
	* memoizeAtLeast
	* memoizeBetween
* there is a trampoline() that can be used with recursion to avoid stack overflow
* Closures implement the isCase() to make them work as classifiers in the grep and switch statements (evaluates to Groovy Boolean)

```groovy
// java 5 
for (ItemType item : list) { /* do something with item */ }

// groovy with java 5
list.each { item -> /* do something with item */ }

// java 8 
list.stream().forEach( item -> /* do something with item */ );

// groovy with java 8
list.stream().forEach { println it }

def log = ''
(1..10).each { counter -> log += counter }
assert log == '12345678910'

log = ''
(1..10).each { log += it }
assert log == '12345678910'

// declaring a closure by assigning it to a variable 
// e.g. def Closure getPrinter() { return { line -> println line } }
def printer = { line -> println line }

Class SizeFilter {
    Integer limit
    boolean sizeUpTo(String value) { return value.size() <= limit }
}
def words = [ 'long string', 'medium', 'short', 'tiny' ]

// GroovyBean constructor calls
SizeFilter filter6 = new SizeFilter(limit:6)
SizeFilter filter5 = new SizeFilter(limit:5)

// method closure assignment
Closure sizeUpTo6 = filter6.&sizeUpTo

// calling with closure
assert 'medium' == words.find(sizeUpTo6)

// passing a method closure directly
assert 'short' == words.find(filter5.&sizeUpTo)

class MultiMethodSample {
    // overloaded methods
    int mysteryMethod (String value) { return value.length() }
    int mysteryMethod (List list) { return list.size() }
    int mysteryMethod (int x, int y) { return x+y }
}

// runtime overload resolution closure example
MultiMethodSample instance = new MultiMethodSample()
Closure multi = instance.&mysteryMethod
assert 10 == multi ('string arg')
assert 3 == multi (['list', 'of', 'values'])
assert 14 == multi (6, 8)

// different ways to perform closures on the following map
Map map = ['a':1, 'b':2]

// 1. simple closure
map.each{ key, value -> map[key] = value * 2 }
assert map == ['a':2, 'b':4]

// 2. closure assignment to a variable
Closure doubler = {key, value -> map[key] = value * 2 }
map.each(doubler)
assert map == ['a':4, 'b':8]

// 3.  referencing an existing method as a closure
def doubleMethod (entry) { entry.value = entry.value * 2 }
doubler = this.&doubleMethod
map.each(doubler)
assert map == ['a':8, 'b':16]

// another example of calling closures 
def adder = { x, y=5 -> return x + y } // similar to: def adder(x, y) { return x + y }
assert adder(4, 3) == 7
assert adder.call(2, 6) == 8
assert adder.call(7) == 12        // when y is missing, use 5 by default

// an example of a method that takes a closure
def benchmark(int repeat, Closure worker) {
    def start = System.nanoTime()
    repeat.times { worker(it) }
    def stop = System.nanoTime()
    return stop - start
}
def slow = benchmark(10000) { (int) it / 2 }
def fast = benchmark(10000) { it.intdiv(2) }
assert fast * 2 < slow

// example of some groovy.lang.Closure methods
def numParams (Closure closure){ closure.getMaximumNumberOfParameters() }
assert numParams { one -> } == 1
assert numParams { one, two -> } == 2
def paramTypes (Closure closure){ closure.getParameterTypes() }
assert paramTypes { String s -> } == [String]
assert paramTypes { Number n, Date d -> } == [Number, Date]

// curry example (not sure how I would ever use this)
def mult = { x, y -> return x * y }
def twoTimes = mult.curry(2) // or you could do def twoTimes = { y -> mult 2, y }
assert twoTimes(5) == 10

// closure composition (f(g(x))) - pointing from the inner to the outer closure
def fourTimes = twoTimes >> twoTimes
def eightTimes = twoTimes << fourTimes
assert eightTimes(1) == twoTimes(fourTimes(1))

// caching results that are called often with the same arguments
def fib //fibonacci
fib = { it < 2 ? 1 : fib(it-1) + fib(it-2) }
fib = fib.memoize() // 20 seconds to 0.001 seconds with caching enabled
assert fib(40) == 165_580_141 

// closure's isCase() implicitly being called for the following
def odd = { it % 2 == 1 }
assert [1,2,3].grep(odd) == [1, 3]
switch(10) { case odd: assert false }
if (2 in odd) assert false
```

* With closures, the programmer can control how references are resolved (to include the ‘this’ reference)
* While you cannot directly set ‘this’ to a different value, you can set a so-called delegate, which will be used when resolving free variables.
* Having the local closure scope, the delegate, and the owner raises the question of who is used for resolving references and, in case of conflicts, in which order. This again can be configured by setting the resolveStrategy to OWNER_ONLY, OWNER_FIRST (default), DELEGATE_ONLY, DELEGATE_FIRST, or SELF_ONLY.

```groovy
class Mother {
    def prop = 'prop'
    def method(){ 'method' }
    Closure birth (param) {
        def local = 'local'
        def closure = { [ this, prop, method(), local, param ] }    // a closure
        return closure
    }
}

Mother julia = new Mother()
def closure = julia.birth('param')                        // declare the closure
def context = closure.call()                            // execute the closure

assert context[0] == julia                                // what 'this' refers to 
assert context[1, 2] == ['prop', 'method']                // bound variables of the closure
assert context[3, 4] == ['local', 'param' ]                // bound variables of the closure
assert closure.thisObject == julia                        // read only 
assert closure.owner == julia                            // read only 
assert closure.delegate == julia                        // scope control (we can set it to a different object)
assert closure.resolveStrategy == Closure.OWNER_FIRST    // scope control​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​​

// the with closure sets the delegate to the map 
def map = [:]
map.with { // delegate is now map (so we don't have to call map every time)
    a = 1 // same as map.a = 1
    b = 2 // same as map.b = 2
}
assert map == [a:1, b:2]

    there are two ways to return from a closure

    The last expression has been evaluated and the result of the evaluation is returned (end return); using return keyword is optional.

    [1, 2, 3].collect{ it * 2 }
    [1, 2, 3].collect{ return it * 2 }

    The return keyword can be used to return from the closure prematurely

    // return ends the current evaluation of the closure (not the collect method) - the next iteration will take place
    [1, 2, 3].collect {
    if (it%2 == 0) return it * 2    
    return it
    }
```

## GroovyBeans and OO - groovy style
### class

* if they are declared with an AST @Immutable (e.g. @Immutable class SomeClass), they will not get a setter method
* AST (abstract syntax tree) can sneak in new method implementations or add, delete, or modify any other code structure (compile-time meta-programming)

```groovy
class BookBean {
    String title 

    BookBean() { println 'constructor' }

    void setTitle(def title) {
        println "title was set to $title"
        this.title = title
    }
}

def groovyBook = new BookBean()    // prints constructor
groovyBook.title = 'test'        // prints title was set to test (showing that setter method was called)
```

### fields

* The default visibility for fields has a special meaning in Groovy. When no visibility modifier is attached to a field declaration, a property is generated for the respective name.
* A GroovyBean is a JavaBean defined in Groovy, but it does things for you:

1. Generates the accessor methods for you
2. Properties (i.e. instance fields) are bindable
3. You can use Groovy short hand to access properties
	a. groovyBook.title = ‘some title’ // Groovy short hand setter (calls setter method)
	b. println groovyBook.title // Groovy short hand (the field is not public)

* When no type and modifier are given, the def keyword must be used as a replacement
* To extend (not override) a field access operator (i.e. subscript operator), provide the following methods:
	* Object get (String name)
	* void set (String name, Object value)
* overriding the get method means to override the dot-fieldname operator. Overriding the set method overrides the field-assignment operator.
* What about a statement of the form x.y.z=something? This is equivalent to getX().getY().setZ(something). 

```groovy
// variable declaration examples
class ClassWithTypedAndUntypedFieldsAndProperties {

    public fieldWithModifier
    String typedField
    def untypedField
    protected field1, field2, field3
    private assignedField = new Date()
    static classField
    public static final String CONSTA = 'a', CONSTB = 'b'

    def someMethod(){
        def localUntypedMethodVar = 1
        int localTypedMethodVar = 1
        def localVarWithoutAssignment, andAnotherOne
    }
}

// referencing fields with the subscript operator
class Counter { public count = 0 }
def counter = new Counter()
counter.count = 1
assert counter.count == 1
def fieldName = 'count'
counter[fieldName] = 2
assert counter['count'] == 2
assert counter.@count == 2

// extending the general field-access mechanism
class PretendFieldCounter {
    public count = 0
    int getCount() { println 'here'; return count }
    Object get (String name) { return 'pretend value' }
    void set (String name, Object value) { count++ }
}
def pretender = new PretendFieldCounter()
println pretender.count // prints 'here', then 0; getCount() is implicitly called
println pretender.x // prints 'pretend value' -- the get() method above is implicitly called
assert pretender.isNoField == 'pretend value'
assert pretender.count == 0
pretender.isNoFieldEither = 'just to increase counter'
assert pretender.count == 1
```

### methods

* all methods are, by default, public
* for methods, return types are optional; if no modifiers or return are supplied, use def (return type is unrestricted - under covers, actual return type will be java.lang.Object (even if you intended a void return type).
* return keyword is optional for the last expression in a method or closure.
* Methods with explicit return type void don’t return a value; closures always return a value.
* The Java compiler fails on missing return statements when a return type is declared for the method. In Groovy, return statements are optional, therefore it’s impossible for the compiler to detect “accidentally” missing returns.
* declarations of exceptions in the method signature are optional, even for checked exceptions
* groovy allows special characters in method names if you quote them. It will even allow GStrings (e.g. “${var}”)

```groovy
// examples of ways to declare methods
class ClassWithTypedAndUntypedMethods {
    // implicit public and args is implicit java.lang.Object (void is optional too)
    static void main(args) {
        def some = new ClassWithTypedAndUntypedMethods()
        some.publicVoidMethod()
        assert 'hi' == some.publicUntypedMethod()
        assert 'ho' == some.publicTypedMethod()
        combinedMethod()
    }

    void publicVoidMethod() { }
    def publicUntypedMethod() { return 'hi' }
    String publicTypedMethod() { return 'ho' }
    private static final void combinedMethod() { }
}
```

### method arguments

* maps and lists can be used to constructor objects (if you don’t provide a constructor, it will implicitly receive map and list ones)
* Groovy supports defaults for arguments
* With Groovy’s method dispatch, you can override the invokeMethod(name, params[]) that every GroovyObject provides.

```groovy
// you can use a map to set the values (no constructors defined)
class BookBean {
    String title 
    String two

    // adding a constructor will break map constructor creation

    // method can include special characters when quoted and doesn't need an explicit return
    String "test it"() {
        "$title $two"
    }
}
def groovyBook = [title:'order', two:'not important'] as BookBean
def anotherBook = new BookBean([two:'order', title:'not important'])
println groovyBook."test it"() // prints "order not important"
println anotherBook."test it"() // prints "not important order"

// you can use a list to set the values (constructor defined)
class BookBean2 {
    String title 
    String two

    // adding this constructor will break map constructor creation
    BookBean2(def title, def two) {
        println 'constructor'
        this.title = title
        this.two = two
    }

    String "test it"() {
        "$title $two"
    }
}
// you can use a map to set the values
def groovyBook = ['order', 'is important'] as BookBean2
BookBean2 anotherBook = ['order', 'is important'] // coercion in assignment
println groovyBook."test it"() // prints "order is important"
println anotherBook."test it"() // prints "order is important"
​
// advanced parameter uses
class Summer {
    def sumWithDefaults(a, b, c=0) { return a + b + c }
    def sumWithList(List args) { return args.inject(0) { sum, i -> sum += i } }
    def sumWithOptionals(a, b, Object[] optionals) { return a + b + sumWithList(optionals.toList()) }
    def sumNamed(Map args){
        ['a','b','c'].each { args.get(it, 0) }
        return args.a + args.b + args.c
    }
}
def summer = new Summer()
assert 2 == summer.sumWithDefaults(1,1)
assert 3 == summer.sumWithDefaults(1,1,1)
assert 2 == summer.sumWithList([1,1])
assert 3 == summer.sumWithList([1,1,1])
assert 2 == summer.sumWithOptionals(1,1)
assert 3 == summer.sumWithOptionals(1,1,1)
assert 2 == summer.sumNamed(a:1, b:1)
assert 3 == summer.sumNamed(a:1, b:1, c:1)
assert 1 == summer.sumNamed(c:1)
```

### miscellaneous

* Java’s default package-wide visibility is supported via the @PackageScope annotation.
* Java 7 introduced a try-with-resources mechanism. At the time of writing, Groovy doesn’t support that syntax. try-with-resources isn’t needed in Groovy, where we have full closure support.
* Groovy provides the additional ?. operator for safe dereferencing; When the reference before that operator is a null reference, the evaluation of the current expression stops, and null is returned
