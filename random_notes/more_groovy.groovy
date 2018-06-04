// ------------------------------------------------------------------------------------------------------------- //
// Strings
// Quoted identifiers (and string types)
// ------------------------------------------------------------------------------------------------------------- //

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

// ------------------------------------------------------------------------------------------------------------- //
// GString interpolation (resolving values)
// ------------------------------------------------------------------------------------------------------------- //

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

// ------------------------------------------------------------------------------------------------------------- //
// GString interpolation using closures - lazy evaluation
// ------------------------------------------------------------------------------------------------------------- //

//When the placeholder contains an arrow, ${?}, the expression is actually a closure expression.

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

// ------------------------------------------------------------------------------------------------------------- //
// GString versus String (they are not the same, although they could appear to be the same)
// ------------------------------------------------------------------------------------------------------------- //

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

// ------------------------------------------------------------------------------------------------------------- //
// Unlike Java, Groovy doesn’t have an explicit character literal; ways to convert it to character
// ------------------------------------------------------------------------------------------------------------- //

// 1. create java static type
char c1 = 'A' 
assert c1 instanceof Character

// 2. use 'as' keyword (type coercion)
def c2 = 'B' as char 
assert c2 instanceof Character

// 3. use static type cast
def c3 = (char) 'C' 
assert c3 instanceof Character

// ------------------------------------------------------------------------------------------------------------- //
// Numbers
// Decimals can use exponents, with the e or E exponent letter
// ------------------------------------------------------------------------------------------------------------- //

assert 1e3  ==  1_000.0        // 1e3 == 1 x 10^3 (scientific notation)
assert 2E4  == 20_000.0
assert 3e+1 ==     30.0
assert 4E-2 ==      0.04    
assert 5e-1 ==      0.5        // 5e-1 == 5 x 10^-1

// ------------------------------------------------------------------------------------------------------------- //
// Arithmetic operators also work with BigInteger and BigDecimal
// ------------------------------------------------------------------------------------------------------------- //

def one = new BigInteger('456')
def two = new BigDecimal('123.45') 
assert (one + two) == 579.45d //the d denotes a BigDecimal literal, but its optional
assert (one + two) instanceof java.math.BigDecimal

// ------------------------------------------------------------------------------------------------------------- //
// Division
// ------------------------------------------------------------------------------------------------------------- //

// division produces double if operand float or double, and BigDecimal otherwise
assert (1.0f / 0.2f) instanceof java.lang.Double
assert (5L / 2i) instanceof java.math.BigDecimal

// so how do we get integral type from integral division (java truncates)
assert (5L).intdiv(2i) instanceof java.lang.Long

// ------------------------------------------------------------------------------------------------------------- //
The power operator (in Java Math.pow(x,y))
// ------------------------------------------------------------------------------------------------------------- //

assert    2 ** 3    instanceof Integer        //  2^3 = 8
assert   10 ** 9 == 1_000_000_000

// the result can't be represented as an Integer or Long, so return a BigInteger
assert  100 ** 10    instanceof BigInteger 
assert  100 ** 10 == 1e20                     // 1 x 10^10

// ------------------------------------------------------------------------------------------------------------- //
// Boolean
// Groovy Truth
// ------------------------------------------------------------------------------------------------------------- //

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

// ------------------------------------------------------------------------------------------------------------- //
// Lists, Arrays, and Maps
// 
// Groovy uses a comma-separated list of values, surrounded by square brackets, for list and arrays.  This is true for maps as well, except each value will be a key:value pair.
// Lists
// ------------------------------------------------------------------------------------------------------------- //

// homogeneous list
def numbers = [1, 2, 3]
assert numbers instanceof java.util.List

// heterogeneous list
def manyTypes = [ 1, "a", true ]
assert manyTypes instanceof java.util.ArrayList

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

Arrays need to be coerced or casted as such (otherwise they’ll be Lists)

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

// ------------------------------------------------------------------------------------------------------------- //
// Maps
// ------------------------------------------------------------------------------------------------------------- //

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
