Run the application using AppStarter main method.

You can access in memory H2 DB on localhost:8080/console

In the app are 3 GET endpoints:
1. /populate - populates DB with initial data
2. /courses - retrieves courses with more than 3 students
3. /courses2 - retrieves courses with student "Jimmy"

Tasks for you:
1. Add property "telephoneNumber" to Teacher. Telephone number should be validated to be at least as 10 digits number. 
You can optionally add additional validations to allow format (xxx)xxx-xxxx or international phone format.
2. Add entity "Room". Room has only ID and it's location (String).
3. Make relationship between Room and Course. Choose correct type of relationship. Ignore time or other constraints. 
2. Code on line 51 in the SuperRespository class does not work. Fix it.