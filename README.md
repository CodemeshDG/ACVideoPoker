# AC Video Poker
A faithful recreation of Jacks or Better video poker.

## What is it?

This is a full recreation of user interface and experience of the popular video poker casino game Jacks or Better. The user interface is recreated through a complex layout combination of ConstraintLayout, LinearLayout, and layout guidelines to ensure visual consistency through various screen resolutions.

## What did I practice?

### SoundPool
Audio is extremely important for recreating a casino game experience. I used SoundPool to define the sounds to be played during gameplay and allow the user to modify their volume with a volume button, just like in the real casino game interface. Using SoundPool also allows for sounds to easily overlap and repeat.

### AssetManager
I used AssetManager to load not only sound assets for the SoundPool, but also for loading card face and back images.

### Runnables & Handlers
This was the first project where I used Runnable and Handler, and I found them to be very helpful in recreating the visual effects of the casino game. I used a custom Handler and Runnable with `postDelay()` in order to animate the cards being drawn on screen. The user can change the game speed, just like in the real casino game, and the value put into the method will influence the speed of the delay. Because the `postDelay()` calls the same Runnable, code is include to figure out when to stop calling that method. I also created a custom Runnable and Handler to flash the "game over" TextView on and off until a new game is started.

I also attempted to use a Runnable to animate credits being deposited into the player's bank. However, the results were not acceptable to my liking, so I am currently not using that custom Runnable in the code until I can modify it appropriately.

### BigDecimal
BigDecimal is used throughout the code when dealing with credit amounts, which are in dollars and cents. BigDecimal keeps the amounts rounded correctly for my purposes and avoids the situations that doubles create, such as $1.05 + $1.05 = $2.09.

### Custom TextView class & Button drawable
I utilized a custom TextView class which allows for outlining and drop shadows. I also wrote a custom drawable for the buttons so that they are colored and shaped depending on the state of the button (focused/enabled). This helps recreate certain visual elements from the casino game.

### Layout guidelines
This is the first app where I used layout guidelines in order to set proportions of the screen to contain certain Views. This helps maintain visual consistency between screen resolutions.
