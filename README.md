BSG Anagrams Application
============================

As part of our recruitment process, to help us to understand your level of proficiency, we would like you to create a solution for our anagram problem.

Instructions
------------
1. **Read the instructions and implement the algorithm described below**

This assignment requires you to implement and expose an algorithm that counts the **number of anagrams** found within the **included dictionary** for all **word lengths** in the dictionary.

The definition of an anagram can be found here: https://www.google.com/search?q=definition+anagram

Your code should:

- Make use of the included file `Dictionary.txt`
- Cater for words of any length
- Produce the **correct output**, run as **efficiently** as possible and be **production ready**
- As described in the next sections, You'll be required to:
    - Expose this algorithm via a rest service and
    - Consume and display the results of your algorithm in the form: `Words with the character length of x had y anagrams`
    - Include the total time in milliseconds it takes for your algorithm to calculate the required results


NB:

- You can structure your solution in any way that you feel will help optimize execution speed, memory usage etc.
- Feel free to use Google when crafting your solution.
- Document your assumptions as comments in your code.
- This assignment only requires you to count anagrams that are **contained** in the provided dictionary.
- Hint: We are not asking you to calculate all permutations of letters of a single word.


2. **Create a RESTful service that is able to do the following actions against the provided dictionary/collection**

- Add a new word
- Retrieve all words
- Delete a word
- Retrieve a list of anagrams for a given word
- Retrieve a list of the counts of anagrams for all word lengths in the dictionary/collection, please note:
    - This requires you to expose the algorithm described in the previous section
    - Your code should be dynamic enough to take into consideration all activities that have occurred against the dictionary, e.g. words added/deleted

3. **Create a frontend that is able to consume the above service and is able to allow the user to do the following actions against the provided dictionary/collection:**

- Capture and save a new word
- Retrieve and display all words
- Delete a word
- Retrieve and display a list of anagrams for a given word
- Retrieve and display a list of the counts of anagrams for all word lengths in the dictionary/collection. Please note:
    - This should be in the form: `Words with the character length of x had y anagrams`
    - This must also display the total time (in milliseconds) it took to calculate the result

Optional/Bonus Instructions
---------------------------

The following is not required but would be nice to see and help us gauge your understanding of certain concepts.

1. Consider implementing the following when designing your backend

    - Persistence
    - Sorting and pagination
    - Caching
    - Automated tests

2. Consider using free services from a cloud platform to deploy your application


Restrictions
------------

1. Language and Frameworks - see below our preferred languages and frameworks for the submission of your solution

    - Frontend: Angular or React
    - Backend:
        - Java and Spring
        - C#

NB: Feel free to use any combination of the above

Run locally with docker
------------
- docker build -t anagrams-image:latest .
- docker-compose up