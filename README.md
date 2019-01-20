# IOTPublicTransport
##IoT based project for tracking and monitoring system for public transport including usage of Directions API, Google Maps SDK.

Apart from basic monitoring of the public traffic systems, using Hardware such as GSM, GPS and Arduino, there are four major features
of the Android Application.
</br>
<img src="https://github.com/sakshichahal53/IOTPublicTransport/blob/master/Untitled%20Diagram.jpg" height="600" width="1000">
</br>

**1. QR Scanner**: The generated QR scanner produces a gps_id of the driver which on a request produces the information of the driver from the DJango servers.
**2. Nearby Vehicles**: The application notifies the user whenever any vehicle travels within the specified range in case of emergency.
**3. Safety Contacts**: Three conacts are stored to send location information in need.
**4. Journey History**: Uses the coordinates generated while travelling through a path to create Journey History. The source and destination coordinates are stored as (-1,-1) and (+1,+1) respectively.
