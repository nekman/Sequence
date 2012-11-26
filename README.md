Sequence
===============

Util class for collections Inspired by LINQ in C# - http://msdn.microsoft.com/en-us/library/bb341635.aspx
NOTE: This is just a test/lab version and it may contain bugs.

Did this as a lab project, beacuse I wanted to learn how to implement some "good to have methods" when working with collections.

###Examples

#####Filter persons

```java
  
  Predicate<Person> oldPersons = new Predicate<Person>() {
     public boolean match(Person p) {
        return p.age > 50;
     }
  };
  
  List<Person> persons = getPersons();
  
  // has old persons?  
  boolean hasOldPersons = from(persons)
    .skip(50)
    .filter(oldPersons)
    .any();
```

#####Grab items from a collection
```java
  from(1,2,3,4,5)
    .take(2)
    .last(); //2 
    
  from(1,2,3,4,5)
    .skip(2)
    .first(); //3
```