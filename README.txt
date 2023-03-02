IJA 2019/2020
Simulace pohybu autobusů na interaktivní mapě
Autoři:
	Roman Štafl xstafl01 <xstafl01@stud.fit.vutbr.cz>
	Michal Vašut xvasut02 <xvasut02@stud.fit.vutbr.cz>
	
Program simuluje pohyb autobusů jezdících po linkách na mapě.
Ulice, stopky a linky jsou definované v souboru ./data/mapdata.txt
Ulice jsou vykreslené jako čáry, stopky jako čtverečky na ulicích a autobusy jako pohybující se kolečka.
Autobusy začínají na první stopce v lince, jakmile dojedou na poslední stopku tak se otočí a jedou po lince zpět.

Mapu lze posouvat držením tlačítka na myši a zoomovat pomocí kolečka.
Kliknutím na ulici lze změnit stupeň provozu. Stupeň provozu ovlivňuje rychlost autobusů projíždějících ulicí. Pravým kliknutím na ulici se stupeň provozu zhorší, levým sníží. Nelze měnit stupeň provozu pokud ulicí právě projíždí autobus.
Kliknutím na autobus se zobrazí itinerář.
Kliknutím na tlačítka vlevo dole se dá zrychlovat a zpomalovat čas.
