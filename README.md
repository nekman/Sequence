###Sequence

Util class for Java collections inspired by LINQ in C# - http://msdn.microsoft.com/en-us/library/bb341635.aspx
<br />
There are other libraries out there that can be used to querying collections e.g:<br/>
<a href="http://code.google.com/p/lambdaj/">LabmdaJ</a><br />
<a href="http://code.google.com/p/tiny-q/">Tiny-q</a><br />
<br/>
This project aims to match the <a href="http://msdn.microsoft.com/en-us/library/bb341635.aspx">LINQ
</a>-syntax.<br/>

###Quick example
Generate a sequence of numbers
```java
Integer[] numbers = from().range(1, 5).take(4).toArray(); //[1, 2, 3, 4]
```
Select all names from a Person collection.
```java
from(persons).select(new Func<Person, String>() {
  public String map(Person p) {
    return p.getName();
  }
})
.take(10); //First 10 person names in the collection.
```

See more examples bellow.

###Supported methods

[from(Collection<T>)](#from)<br />
[from(T...)](#from)<br />
[concat(Collection<T>)](#concat)<br />
[concat(T...)](#concat)<br />
[any()](#any)<br />
[any(Predicate<T>)](#any)<br />
[all(Predicate<T>)](#all)<br />
[filter(Predicate<T>)](#filter)<br />
[where(Predicate<T>)](#filter)<br />
[map(Func<T, TResult>)](#map)<br />
[select(Func<T, TResult>)](#map)<br />
[skip(int)](#skip)<br />
[take(int)](#take)<br />
[count()](#count)<br />
[last()](#last)<br />
[lastOrDefault()](#lastOrDefault)<br />
[first()](#first)<br />
[firstOrDefault()](#firstOrDefault)<br />
[range(int, int)](#range)<br />
[toList()](#toList)<br />
[toCollection()](#toList)<br />
[toArray()](#toList)<br />
[toSet()](#toList)<br />
[forEach(Action<T>)](#forEach)<br />
[ofType(Class<V>)](#ofType)<br />

[Example of map and filter](#mapFilterExample)<br />


###Initialization

For easiest access, import the 'from' method static:
```java
import static se.nekman.sequence.Sequence.from;
```

The Sequence can also be initialized by using the constructor:
```java
Sequence<String> sequence = new Sequence<String>(Arrays.asList("foo", "bar"));
```

#####<a name="from"></a>from()
```java  
  from(1,2,3,4,5)
    .take(2)
    .last(); //2 
    
  from(1,2,3,4,5)
    .skip(2)
    .first(); //3

  from().range(1,5) // [1, 2, 3, 4, 5]

  List<Person> persons = Arrays.asList(new Person());
  boolean hasPersons = from(persons).any(); //true
  
  String[] names = { "adam", "ben", "ceasar" };
  from(names)
    .take(2)  // ["adam", "ben"]
    .first(); // "adam"

```
#####<a name="concat"></a>concat()

Concatenates two sequences.

```java

   from(1,2,3)
      .concat(4,5)
      .toArray(); //[1, 2, 3, 4, 5]

   Person[] persons = from(persons)
                .concat(getOtherPersons())
                .toArray();

```
#####<a name="any"></a>any()

Determines whether a sequence contains any elements.

```java
   List<Integer> numbers = getNumbers();
   if (from(numbers).any()) {
      // Sequence contain elements
   }
  
   Sequence<Integer> ids = from(getIds());
   if (ids.any()) {
      // Sequence contain elements
   }
   
   Predicate<Person> namesStartsWithA = new Predicate<Person>() {
      public boolean match(Person p) {
         return p.getName().startsWith("a");
      }
   };
   
   if (from(getPersons()).any(namesStartsWithA)) {
      // Sequence contains persons with names that starts with "a"
   }
```  
#####<a name="all"></a>all()

Determines whether all elements of a sequence satisfy a predicate.

```java
 
  Predicate<Person> isOlderThan30 = new Predicate<Person>() {
     public boolean match(Person p) {
        return p.getAge() > 30;
     }
  };

 boolean allPersonsIsOlderThan30 = from(getPersons()).all(isOlderThan30);

```

#####<a name="filter"></a>filter() / where()
Filters a sequence of values based on a predicate.

Note: filter is same as where.
```java

  Predicate<Person> oldPersons = new Predicate<Person>() {
     public boolean match(Person p) {
        return p.getAge() > 70;
     }
  };
  
  List<Person> persons = getPersons();
  
  // has old persons?  
  boolean hasOldPersons = from(persons)    
    .filter(oldPersons)
    .any();

```
#####<a name="map"></a>map() / select()

Projects each element of a sequence into a new form.

Note: map is same as select.
```java
  Func<Person, Integer> getShoeSizes = new Func<Person, Integer() {
      public int map(Person p) {
        return p.getShoeSize();
      }
  };

  Integer[] shoeSizes = from(persons)
     .map(getShoeSizes)
     .toArray();

```
#####<a name="skip"></a>skip()
```java
    from(getPersons())
       .skip(5) // Skip the first five persons in the sequence
       
    from(1, 2, 3, 4, 5)
       .skip(2) // [3, 4, 5]
```
#####<a name="take"></a>take()
```java
    from(getPersons())
       .take(5) // Take the first five persons in the sequence
       
    from(1, 2, 3, 4, 5)
       .take(2) // [1, 2]
```

#####<a name="count"></a>count()

Gets the number of elements in the sequence.

```java 
    int numberOfPersons = from(getPersons()).count();       
```

#####<a name="last"></a>last()

Gets the last element of a sequence.
Throws a EmptySequenceException if the sequence is empty.

```java 
    from("foo", "bar", "baz").last(); // "baz" 

    from("foo", "bar", "baz").skip(4).last(); // throws EmptySequenceException   
```

#####<a name="lastOrDefault"></a>lastOrDefault()

Gets the last element of a sequence, or default if the sequence is empty.

```java 
    from("foo", "bar", "baz").lastOrDefault(); // "baz" 

    from("foo", "bar", "baz").skip(4).lastOrDefault(); // null
```

#####<a name="first"></a>first()

Gets the first element of a sequence.
Throws a EmptySequenceException if the sequence is empty.

```java 
    from("foo", "bar", "baz").first(); // "foo" 

    from("foo", "bar", "baz").skip(4).first(); // throws EmptySequenceException   
```

#####<a name="firstOrDefault"></a>firstOrDefault()

Gets the first element of a sequence, or default if the sequence is empty.

```java 
    from("foo", "bar", "baz").firstOrDefault(); // "foo" 

    from("foo", "bar", "baz").skip(4).firstOrDefault(); // null
```
#####<a name="range"></a>range(int, int)

Generates a sequence of integral numbers within a specified range.

```java 
    from().range(1, 3); //[1, 2, 3]

    from().range(1, 1000); //[1, 2, 3, ... , 1000]    
```

#####<a name="toList"></a>toList()

Returns the sequence as an List<T>.

```java 
    List<Person> persons = from(getPersons()).take(5).toList();

    Person[] persons = from(getPersons()).take(5).toArray();

    Collection<Person> persons = from(getPersons()).take(5).toCollection();

    Set<String> persons = from("foo", "foo", "bar", "bar").toSet(); //["foo", "bar"]
```

#####<a name="forEach"></a>forEach()

Performs the specified action on each element in the sequence.

```java
  //Create a action
  Action<Person> updatePersonAge = new Action<Person>() {    
    public void execute(Person p) {
      p.age += 10;
    }           
  };

  from(getPersons()).forEach(updatePersonAge);
```
#####<a name="ofType"></a>ofType()

Filters the elements of an sequence based on a specified type.

```java

// Get all properties (Hashtable<Object,Object>)
Properties props = getProperties();

for (String s : from(props.keySet()).ofType(String.class)) {
    // Do something with the string
}
```

#####<a name="mapFilterExample"></a>Filter/Where Map/Select examples

Note that the filter and map function is equal to select and where. 

```java
  //Select function
  Func<Person, String> personName = new Func<Person, String>() {    
    public String map(Person p) {
      return p.getName();
    }           
  };
  
  //Where predicate
  Predicate<String> hasLongNames = new Predicate<String>() {
     public boolean match(String s) {
        return s.length() > 10;
     }
  };
  
  // All persons with long names
  String[] personWithLongNames = from(getPersons())
              .concat(getOtherPersons())
              .select(personName)
              .where(hasLongNames)
              .toArray();
              
  // First five with long names         
  List<String> firstFive = from(personWithLongNames)
              .take(5)
              .toList();
                
  // Sequence of strings
  Sequence<String> seq = from(firstFive);
  
  boolean hasElementsInSequence = seq.any();  
  String first = seq.firstOrDefault();
  String last = seq.lastOrDefault();
  
```