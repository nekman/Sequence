Sequence
===============

Util class for collections inspired by LINQ in C# - http://msdn.microsoft.com/en-us/library/bb341635.aspx

#####NOTE
I know that there are a bunch of frameworks out there (e.g LabmdaJ - http://code.google.com/p/lambdaj/)
But I did this as a lab project, beacuse I wanted to learn how to implement some simple "good to have methods"
when working with collections.

Doesn't uses any reflection, just simple Java.

###Usages and examples


#####Grab items from a collection
```java
  from(1,2,3,4,5)
    .take(2)
    .last(); //2 
    
  from(1,2,3,4,5)
    .skip(2)
    .first(); //3
```
#####Filter persons by age
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

#####Work with a collection
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
###TODO
forEach()
sort()