# bicloo-localisation
Application android permettant de  localiser les stations de vélos et de se déplacer avec.

Première version permettant de localisation les stations bicloo.

- Liste des stations via une liste (bouton switch de liste vers map en haut à droite).
- Marqueur sur la carte permettant de voir les stations actuellement à cours de vélos ou places.
- Bottom sheet sur la selection d'un marqueur donnant quelques informations sur la station (disponible aussi sur la liste).
- Possibilité de trier via les options disponible dans le menu coulissant à gauche. Permet de n'afficher qu'un partie des stations.
- Possibilité de définir un point de départ et d'arrivé et de lancer un itinéraire entre les 2 stations disponible prêt de ces 2 points.


Améliorations à faire :
- Ajouter un popup avant de lancer la recherche d'itinéraire. Ce dernier listerait plusieurs stations proches des points de départ et d'arrivé. Permettant ainsi de faire un choix avant de lancer l'itinéraire entre deux stations.
- Ajouter la langue anglaise sur l'application (normalement avec le strings.xml celà devrait se faire assez facilement).
- Permettre de lancer un itinéraire en mode hors ligne (actuellement une requête est necessaire à voir s'il est possible de faire autrement).
- Mieux gérer la géolocalistation lorsque le gps n'est pas activé (actuellement c'est forcé sur les coordonnées de Nantes si le gps est éteind)
