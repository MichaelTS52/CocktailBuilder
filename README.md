**CS50-edx 2020 Final Project - Android track**

This is my final project as part of CS50 through edx: working title _Cockail Builder/Build Me A Cocktial_

**About**
In essence this is a simple recipe app that allows you to track ingredients, recipes and what is currently "makeable"

Using androids tabbed layout we have 3 main fragments with additional dialog fragments for adding new items.
We can add bar items to keep track of what we currently have avalible in the MyBar tab. See what is currently "makeable" in the middle tab, and see a full list of all saved recipes in the final tab.

data is stored using 3 SQLite tables. One for ingredients, one for recipes, and a relational table. These are acessed using sqliteOpenHelper and a cursor.


You can see the project in action at: https://youtu.be/DOq6H7gYmBE

**About me**
I'm Michael, a 24yo physics grad from Scotland looking to break into sofware engineering/dev as a career. Imporving my skills with CS50 during 2020 pandemic. I have some basic scripting experience (python/matlab) from university but this is my first major project, especially in java. I had 0 experience in android a couple months ago. Though I understand this is probably trivial to most.

**acknowledgements**
A lot from this project was help through various online tutorials. In particular, tutorialpoint.com and codinginflow.com as well as countless stackoverflow posts.


_**Plans for improvement**_

- various bug fixes
- add delete ingredient delete functionality in recipe add dialog
- add edit options for ingredients and recipes
- add search functionality
- add sort functionality
- add autocomplete functionality to new bar item interface
- change SQL to allow real numbers for rating and ingredient amounts

**Known bugs**
- Issues retrieving ingredients list after notes save
- slow update to makeable recycler view after dataset change
- Issues with wrong ingredients list getting locked to a recipe when spam clicking (currently sloved by limiting time between clicks)
- change recipe add form funciton to add on keyboard enter instead of automatically (inconsistent UX)