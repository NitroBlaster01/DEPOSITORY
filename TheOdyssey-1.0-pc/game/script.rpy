# The script of the game goes in this file.

# Declare characters used by this game. The color argument colorizes the
# name of the character.

define a = Character(_("Odysseus"), color="#ad42f4")
define m = Character(_("Me"), color="#272428")
define t = Character(_("Telemachus"), color="#0b60e8")

image happy = "kg110001.png"
image hello = "kg110000.png"
image sheepish = "kg110002.png"
image sigh = "KG110006.png"
image dissapointed = 'kg110004.png'
image serious = 'kg110005.png'
image zooms = 'kg110024.png'
image surprise = 'kg110013.png'
# The game starts here.

label start:
    scene black
    show text "This modern day adaptation uses Books 17 and 18 from Homer's The Odyssey. It adapts some of the 'tests' that Odysseus dishes out to the people he encounters.\n You will take the place of some of these people." with fade
    $ renpy.pause(5)
    show text "Eumaeus" with fade
    $ renpy.pause(1.5)
    play music ["As the Night Moves.mp3"] fadein 1.0
    scene bg1 with fade
    "Today's a nice day. The birds are singing and the sun is shining. "
    "I miss Odysseus. Telemachus needed a father, and Odysseus couldn't be there for him."
    "I tried for that boy."
    "I hope no one actually manages to win Penelope's hand."
    "Those suitors; their pride and violence hits the iron skies. I can only feel sorry for the family."

    show hello with fade
    "This guy just came out of nowhere!"
    "He looks oddly familiar, and even godlike."
    a "Hello, nice to meet you!"

    show happy
    hide hello

    a "My name is Ody... uhh Aithon!"

    show sheepish
    hide happy

    a "Sorry to ask you this out of the blue, but do you happen to have $2 to spare?"

    show sigh
    hide sheepish

    a "My car ran out of gas, and I left my wallet at home. I need to get over to Penelope's."

    menu:
        "What should I say?"
        "Sure.":
            jump scenario1
        "No.":
            jump scenario2

label scenario1:
    m "Sure, I happen to have a few dollars I can spare."
    m "Also, about Penelope. Be careful not to mingle with her group of suitors. They are a vicious bunch."
    show happy
    hide sigh
    a "Thanks a lot. You don't know how much that helps. I'll try not to mess with the suitors."
    hide happy with fade
    "I get this odd feeling that I did something right."
    jump second

label scenario2:
    m "Sorry, I don't have anything I can spare at the moment."
    m "Though some advice, try not to mess with the suitors; some of them aren't too right in the head. "
    show dissapointed
    hide sigh
    a "It's fine man, no worries. I'll take your advice though. Thanks."
    hide dissapointed with fade
    "I feel like I should have given him some money."
    jump second

label second:
    scene black with fade
    show text "Amphinomus" with fade
    $ renpy.pause(1.5)

    scene bg2 with fade
    "Man this guy seems pretty nice."
    "I mean he put Arnaeus in his place. That guy totally deserved it."

    show hello with fade

    "Oh here he comes."
    a "Hello, Amphinomus."
    m "A lot of respect for how you dealt with the other guy. I will toast to that."
    show sheepish
    hide hello
    a "Oh thanks for that."
    show serious
    hide sheepish
    a "Oh yeah, I have something to tell you."
    show zooms
    hide serious
    a "I think Odysseus is coming back, and I don't think he'll be satisfied until blood has flowed. "
    m "What? I don't think that's possible. The man straight up left; why would he come back?"
    a "You seem like a man of good sense, so I wanted to warn you. You might want to leave."
    menu:
        "What should I do?"
        "I'll leave.":
            jump leave
        "I'll stay":
            jump stay
label leave:
    m "You know what? You've convinced me. Something does seem awfully suspicious with this whole situation."
    show happy
    hide zooms
    a "Great!"
    a "Believe me, you won't regret it."
    hide happy with fade
    "I feel like a great weight has been lifted off of me."
    "Wait how did he know my name already?"
    jump third

label stay:
    m "I think I'd rather stay. I just don't feel that I should bank this decision off of your words alone."
    show serious
    hide zooms
    a "Your choice man."
    a "I was just giving you a heads up."
    hide serious with fade
    "His words rung in my head."
    "I feel like there are grave forebodings for my future."
    "Wait how did he know my name already?"
    jump third

label third:
    scene black with fade
    show text "Eurymachus" with fade
    $ renpy.pause(1.5)
    scene bg3 with fade
    "I'm starting to really hate this guy."
    "He just barges into our time with Penelope and then decides to ruin the party."
    "I mean the man even scared away one of the hot maids."
    "I wonder..."
    m "Hey dude, I bet you would love to work under me. I mean, I would give you a decent wage, and even benefits."
    show serious
    a "Oh Eurymachus, you never cease to prove yourself a brutal fool. I can't wait for Odysseus to wipe that damn smirk off of your face."
    show happy
    hide serious
    m "Why you little..."
    m "Do you have no fear in your heart?"
    menu:
        "What should I do?"
        "Make him pay":
            jump gun
        "Calm down":
            jump resolve
label gun:
    m "Guess I'll make you fear something then."
    show surprise
    hide happy
    a "Jesus, man put down the gun, you could seriously hurt someone with that."
    m "That's the point."
    "I fired"
    "..."
    "..."
    "I didn't hit him"
    show serious
    hide surprise
    a "Look what you did now. You hit a completely innocent guy."
    t "WHAT THE HELL IS GOING ON HERE?!"
    hide serious with fade
    "Goddammit"
    jump finale

label resolve:
    m "..."
    m "Sorry for getting so worked up there."
    m "You just seriously piss me off."
    show serious
    hide happy
    a "Believe me, you deserve it."
    "He's right."
    "What am I doing here?"
    hide serious with fade
    "Still, it feels like my fate is sealed, like my days are already numbered."
    jump finale
    stop music fadeout 1.0
label finale:
    scene black
    show text "And so completes some of the experiences of the people Odysseus tested. I hope that you enjoyed the ride." with fade
    $ renpy.pause(3)
    show text "Good Bye" with fade
    $ renpy.pause(3)
    show text "Made by Cool Guy Khandaker" with fade
    $ renpy.pause(2)
    return
