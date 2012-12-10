Sequence
===============

Util class for Java collections inspired by LINQ in C# - http://msdn.microsoft.com/en-us/library/bb341635.aspx

#####NOTE
Did this as a lab project. 
I know that there are other frameworks out there (e.g LabmdaJ - http://code.google.com/p/lambdaj/).

###Supported methods
```java
from(Collection<T>)
from(T...)
concat(Collection<T>)
concat(T...)
any()
all(Predicate<T>)
any(Predicate<T>)
filter(Predicate<T>)
map(Condition<T, TResult>)
skip(int)
take(int)
count()
last()
lastOrDefault()
firstOrDefault()
first()
range(int, int)
toList()
toCollection()
toArray()
forEach(Action<T>)
ofType(Class<V>)
```
###Methods that should be implemented
```java
sort() / orderBy()
thenBy()
toSet()
toMap()
```
###Usages and examples


#####Generate and getting items from a collection
```java
  from(1,2,3,4,5)
    .take(2)
    .last(); //2 
    
  from(1,2,3,4,5)
    .skip(2)
    .first(); //3

  from().range(1, 5).toList(); // [1, 2, 3, 4, 5]
```
#####Filter "persons" by age
```java
  
  Predicate<Person> oldPersons = new Predicate<Person>() {
     public boolean match(Person p) {
        return p.getAge() > 50;
     }
  };
  
  List<Person> persons = getPersons();
  
  // has old persons?  
  boolean hasOldPersons = from(persons)
    .skip(50)
    .filter(oldPersons)
    .any();
```

#####Filter and map
```java
  //Map condition
  Condition<Person, String> personName = new Condition<Person, String>() {  	
		public String map(Person p) {
			return p.getName();
		}						
	};
  
  //Filter predicate
  Predicate<String> longNames = new Predicate<String>() {
     public boolean match(String s) {
        return s.length() > 10;
     }
  };
  
  // All persons with long names
  String[] personWithLongNames = from(getPersons())
              .concat(getOtherPersons())
              .map(personName)
              .filter(longNames)
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
#####forEach
```java
  //Create a action
  Action<Person> updatePersonAge = new Action<Person>() {    
    public void execute(Person p) {
      p.age += 10;
    }           
  };

  from(getPersons()).forEach(updatePersonAge);
```
#####ofType
```java

// Get all properties (Hashtable<Object,Object>)
Properties props = getProperties();

for (String s : from(props.keySet()).ofType(String.class)) {
    // Do something with the string
}
```