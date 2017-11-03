# Flirds
MKII of the Flirds simulation rewritten in Android Studio with improved usability and functionality
A simulation of evolution and genetics.

---

# **About**
Flirds is a theoretical simulation of evolution to show the development of unsupervised neural networks in combination with a genetic algorithm through breeding. (Basically you can watch some creatures move around and eat each other).

Each flird has a `brain` which is essentially a neural network, this allows the Flird to decide what to do, these options include:
* Move away from a potential predator
* Move towards a potential prey
* Move towards a potential mate
* They will move randomly if the output isn't within the range (0-1)

The inputs for the brain are as follows (all inputs are mapped to the range (0-1)):
* A bias (which is always equal to 1)
* The difference in aggression between this an the closest flird
* 1-Aggro where aggro represents my aggression mapped from the range (0-100) to the range (0-1) this means that higher aggression will contribute to being more likely to chase prey
* Hunger - Lower hunger theoretically encourages a Flird to chase prey when it's hungry

---

# **To do**
Our Trello board can be seen [here](https://trello.com/b/icAxEHQc/flirds)
This project is still early in development so you can expect many changes in the coming months.
An APK file should be released in the next couple of weeks however it won't be representative of the final project.


# _How to support_
Feel free to post new ideas and possible changes, all are welcome.

(C) CodingStudiosUK Team
