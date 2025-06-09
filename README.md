# SIX-coding-exercise
SIX Coding Exercise - SpaceX Dragon Rockets Repository

Assumptions made in task:
- Multiple rockets can have the same name, but unique ID
- Mission names are unique 


Scenarios that could be considered but were not fully reflected in the code:
- If someone is adding multiple rockets to a mission, and some of them are already assigned,
 should we throw an exception and stop processing or skip them
- Someone will pass a not exising status of Rocket/Mission
- Changing the mission status should be allowed to any of the supported or only to ENDED