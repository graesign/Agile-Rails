README

Owkay,
Het doel van deze test is om een bruikbare Canvas te maken om later in de GUI te stoppen.

Tot zover:
-je kunt NodeSprites toevoegen, die extenden de basis RailCabSprite
-je kunt NodeSprites selecteren
-ik geef elke NodeSprite een richting. 
-NodeSprites trekken een soort lijntje naar het middelpunt van de volgende Sprite
-de TrackBuilder gaat op basis van de richting van een NodeSprite de volgende toekennen als "nextNode"
-een SwitchNodeSprite, die twee buren heeft. Kan ook naar de één of de andere wijzen.
-de SwitchNodes hebben twee buren, de TrackBuilder er op aangepast


Wat komt:
-een treintje dat rondjes rijdt
-wissels omzetten door te klikken
Dit hoop ik voor maandag te doen


En dan:
-StationNodes die zich "groeperen" en als geheel worden geselecteerd.





PROBLEMEN:
RailCabSprite.java is misschien verwarrend. Het gaat hier niet om een trein maar om een soort basis klasse om mee te tekenen. RailCab.java is de trein klasse in wording.

Hier en daar gebruik ik een Dimension object voor een coordinaat. Dit zou eigenlijk een Point object kunnen zijn.

De RailCabSprite klasse word een beetje een rotzooitje ( gemakshalve ), ik moet wat functies Overriden in de ervende klassen.

Soms ( soms niet ) flikkert het scherm een beetje. Heeft iemand veel verstand van Dubble Buffering, Page Flipping ect.? 
Misschien kan ik alle statische graphics ( wat maar één keer getekend hoeft te worden ) als een soort Image gebruiken, hoeft dat niet steeds geupdate ( scheelt ook veel ififififif blokken bij een update ).

