# Customisable Random Map Generation

```
Customisable Random Map Generation Ver. 1

Created by Jaleel "Jantoom" Abdur-Raheem
```

Random generation is customisable in the sense that floor plans and room interiors can be prefabricated. The algorithm will pick from these JSON files at random.

There are four levels of random abstraction. A randomly-picked floor plan will randomly populate its room plans with room types. A room type will random-pick its interior design, that may include declarations for randomly-generated entities.

It is important that custom room floor plans and room interiors adhere to the dimensions specified in `room_properties.json`. This file should not be edited to ensure that homes generated resemble real life home designs.