
/*
* General


Boomerang: Notes fly in from the top (or bottom if reverse), decelerate towards the bottom, then reverse and accelerate back towards the note field.
Effects

Most effects can be amplified if you use them in the #ATTACKS field in a simfile. They can also be stacked.

Dizzy: The notes spin in counter clock

Twirl: The notes spin in 3D

Confusion: The notes and note receptors spin.

Drunk: The notes and note receptors sway to the left and right

Beat: The note and note receptors bounce left and right to the beat of the song

Tornado: notes fly in to the left, then to the right as they line up with the receptors

Mini: Makes the notefield small

Big: Makes the notefield big

XMode: The notes come in from a certain angle (You probably want to use this with #ATTACK so you can amplify it)

Bumpy: The notefield is "bumpy" in that the notes ripple towards and back in your direction.

Invert: The receptors are inverted. (Down, Left, Right, Up)

Flip: the receptors are flipped. (Right, Up, Down, Left)
Appearance

Visible: Notes are completely visible.

Hidden: Notes become invisible 3/4ths of the way before reaching the receptors.

Sudden: Notes are invisible, then become visible 1/4th of the way towards the receptors.

Stealth: Notes are completely invisible.

Blink: Notes flash between visible and invisible.

These two aren't part of StepMania's default, but some custom themes implement them:

Sudden+: Cover the bottom half of the notefield with a window or image that can be adjusted with EffectUp or EffectDown.

Hidden+: Cover the top half of the notefield (where the receptors are) with a window that can be adjusted with EffectUp or EffectDown.
Turn

(This changes the notes themselves instead of how they appear)

Mirror: The chart is mirrored.

Backwards: The chart is mirrored?

Left: The chart is turn left.

Right: The chart is turn right.

Shuffle: The chart is randomized, but tries to prevent jacks(?).

Cement Mixer/SuperShuffle: The chart is randomized completely.

Soft Shuffle: Left and right are flipped.
Insert

Wide: Extra jumps on 1/4 beats.

Big: Adds notes on 1/8th beats.

Could someone add descriptions for the rest of the moddifiers in this section please? Thanks.
Remove

Removes notes to make the song easier.

Little: Removes notes requiring you to hit out of 1/4 beat. Known as CUT on DDR series.

No Jumps: Removes notes requiring you to hit two arrows at the same time.

No Hands: Removes notes requiring you to hit three arrows at the same time.

No Quads: Removes notes requiring you to you hit four arrows at the same time.

No Stretch:

No Rolls: Removes roll notes.

No Lifts: Removes lift notes.

No Fakes: Removes fake notes.
Scroll

Normal: Notes come from bottom to top.

Reverse: Notes come from top to bottom.

Split:

Alternate: The orientation of the receptor switches every time, so the first one is upright, the second one is down, the third is up, the fourth is down.

Cross: The outer receptors are upright, and the inner receptors are at the bottom.

Centered: The receptors are vertically centered.
Holds

(Known as "Frezze Arrows" on DDR series)

No Holds: Removes hold notes to make the song easier.

Planted: Adds more holds. A lot of them.

Twister: Adds more holds, but allows for the possibility of more than two holds at a time.

Holds To Rolls: All holds are now rolls.
Mines

Could someone add descriptions for the moddifiers in this section please? Thanks.
Attacks

On: If the simfile has an #ATTACKS section, it will play scripted modifiers during gameplay.

Random Attacks: Randomly add and remove different modifiers during gameplay.
Hide

These can be stacked.

Dark: The note receptors are invisible.

Blind: Your combo and judgements are invisible.

Cover: The BGA is darkened.
Extra
Life Type

Note: Some themes have this option in Player Options instead of Song Options. Aslo, may change the options depending on theme.

Bar: Normal lifebar type. Known as "Normal" on DDR series.

Battery: Miss a note and lose a life. If you run out of lives, you fail the song.

Some themes have 4 LIVES, that is same as Battery + 4 lives, and RISKY, that is same as Battery + 1 life, replacing to the Battery option and both "Bar Drain" and "Battery Lives" modifiers.
Bar Drain

Normal: Self explanatory. Known as "Normal-Drain" internally.

No Recover: Don't recover life upon scoring well.

Sudden Death: Same as Battery + 1 life (RISKY on DDR series). If you miss a note you fail.
Battery Lives

Self explanatory.

Note: "1 life" = RISKY on DDR series.
Fail type

Immediate: Fail when the lifebar or battery is empty. Known as "FailImmediateContinue" internally.

Delayed:

Fail at end: Fail at the end of the song if your lifebar or battery is empty. Known as "FailAtEnd" internally.

Off: You can't fail. Known as "FailOff" internally.*/

package com.example.rodrigo.sgame.Player;

import android.opengl.Matrix;

public class Attack {
    double len,time;
    String Effect;

    public Attack (String[] Data){

    }






}
