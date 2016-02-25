<map version="0.9.0">
<!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->
<node CREATED="1456155830770" ID="ID_965301578" MODIFIED="1456416143204" TEXT="PrivBioMTAuth">
<richcontent TYPE="NOTE"><html>
  <head>
    
  </head>
  <body>
    <p>
      Privacy Preserving Biometrics based Authentication to Remote Services from Mobile Devices.
    </p>
    <p>
      Privacy Preserving Biometrics based Authentication Protocl for Authenticating to Remote Services from Mobile Devices.
    </p>
    <p>
      This could be one project (PrivBioMTAuth) under PrivBioAuth. The other could be: PrivBioGeneAuth. See how to use genomics for privacy preserving authentication - discuss this with professor in a later meeting.
    </p>
    <p>
      
    </p>
    <p>
      Lot of biometrics based authentication mechanisms are defined for authenticating to devices. Once authenticated into the device, different services that the user accesses are already logged in with username/password security. In such cases, critical remote services are relying on the device biometric authentication, which is not usually strong.
    </p>
    <p>
      
    </p>
    <p>
      This shows the requirement for remote services to have their own authentication of user
    </p>
    <p>
      to make sure that the genuine user invokes some request, with strong verification, beyond
    </p>
    <p>
      usernam/password, and without relying on device autentication.
    </p>
    <p>
      This should be a standard mechanism.
    </p>
    <p>
      
    </p>
    <p>
      <b>Contributions: </b>
    </p>
    <ul>
      <li>
        Secure protocol for remote authentication using biometrics. Preserves good properties of biometrics (i.e: uniqueness). Avoids non-desirable properties of biomertics (i.e: non-repeatability, non-revocability). &#160;
      </li>
      <li>
        Prototype implementation that is a proof of concept. That can be integrated to any app. &#160;
      </li>
      <li>
        Security Analysis and Performance Analysis.
      </li>
    </ul>
  </body>
</html>
</richcontent>
<node CREATED="1456156008271" ID="ID_1168541077" MODIFIED="1456157601170" POSITION="right" TEXT="Remote Authentication">
<node CREATED="1456156322597" ID="ID_471105895" MODIFIED="1456156756357" TEXT="From Mobile Device">
<node CREATED="1456156335312" ID="ID_1853598149" MODIFIED="1456159231099" STYLE="fork" TEXT="Standard Mechanim">
<richcontent TYPE="NOTE"><html>
  <head>
    
  </head>
  <body>
    <p>
      that any app resides in user's device-communicating with the remote service can integrate easiliy.
    </p>
    <p>
      
    </p>
    <p>
      See this could be developed as a service in Android which could be invoked by other apps.
    </p>
  </body>
</html></richcontent>
<font NAME="SansSerif" SIZE="12"/>
</node>
<node CREATED="1456156500646" ID="ID_584417830" MODIFIED="1456156510450" TEXT="Address threats in mobile device"/>
<node CREATED="1456159238373" ID="ID_1970757102" MODIFIED="1456159288506" TEXT="See how Android and iOS do biomt auth">
<icon BUILTIN="messagebox_warning"/>
</node>
<node CREATED="1456159832503" ID="ID_797834982" MODIFIED="1456159850587" TEXT="See how bank apps work in mobile devices">
<icon BUILTIN="messagebox_warning"/>
</node>
<node CREATED="1456350821263" ID="ID_1350930906" MODIFIED="1456350876094" TEXT="Implement TrustZone usage">
<icon BUILTIN="help"/>
</node>
</node>
<node CREATED="1456156412610" ID="ID_705595909" MODIFIED="1456156808388" TEXT="Address threats in remote auth"/>
</node>
<node CREATED="1456156037921" ID="ID_64932047" LINK="http://link.springer.com/article/10.1007/BF02351717#page-2" MODIFIED="1456156791735" POSITION="left" TEXT="Zero Knowledge Proof">
<node CREATED="1456157387534" ID="ID_1511546524" MODIFIED="1456159671527" TEXT="How to make x from biometrics">
<richcontent TYPE="NOTE"><html>
  <head>
    
  </head>
  <body>
    <p>
      In other words, how to make biometrics repeatable, while preserving uniqueness.s
    </p>
  </body>
</html></richcontent>
<node CREATED="1456157494600" ID="ID_831000120" MODIFIED="1456157875206" TEXT="Fuzzy Zero Knowledge">
<icon BUILTIN="idea"/>
</node>
<node CREATED="1456157512611" ID="ID_952294155" MODIFIED="1456416263419" TEXT="Machine Learning">
<richcontent TYPE="NOTE"><html>
  <head>
    
  </head>
  <body>
    <p>
      Do we need to send the classifier file? Isn't it sufficient to send the equation, parameters?
    </p>
    <p>
      
    </p>
    <p>
      Try to use at least two predictive models and compare them in terms of performance and the artifacts needed to re-predict at authentication.
    </p>
    <p>
      
    </p>
    <p>
      Also, compare and contrast the usage of binary classifier vs multi class classifier in terms of accuracy, performance, security and work for IDP (coz binary classifer requires IDP to create classifier for each individual).
    </p>
  </body>
</html>
</richcontent>
</node>
<node CREATED="1456157884434" ID="ID_108906632" MODIFIED="1456157894066" TEXT="Fuzzy Commitment"/>
<node CREATED="1456157898617" ID="ID_1077940242" MODIFIED="1456157903231" TEXT="Fuzzy Vault"/>
</node>
</node>
<node CREATED="1456157614774" ID="ID_945181756" MODIFIED="1456157628592" POSITION="right" TEXT="Canceleble Biometrics"/>
<node CREATED="1456157941831" ID="ID_312794241" MODIFIED="1456350895007" POSITION="left" TEXT="Related Work">
<node CREATED="1456157956152" ID="ID_33469545" LINK="http://ieeexplore.ieee.org/xpls/abs_all.jsp?arnumber=5634541&amp;tag=1" MODIFIED="1456158524137" TEXT="Practical multi-factor Biometric Remote Authenication">
<node CREATED="1456158559455" ID="ID_932932892" LINK="http://cosec.bit.uni-bonn.de/fileadmin/user_upload/publications/pubs/sar10d.pdf" MODIFIED="1456158578262" TEXT="another link"/>
</node>
<node CREATED="1456350907073" ID="ID_1713100324" MODIFIED="1456351269362" TEXT="Efficient Privacy Preserving Biometrics Identification - Yan Huang">
<richcontent TYPE="NOTE"><html>
  <head>
    
  </head>
  <body>
    <p>
      Another way of biometrics remote authentication is: homomorphic encryption.
    </p>
    <p>
      I should compare the performance of Zero Knowledge with Homomorphic Encryption.
    </p>
  </body>
</html></richcontent>
</node>
</node>
<node CREATED="1456159680463" ID="ID_884844264" MODIFIED="1456159687319" POSITION="right" TEXT="Analysis">
<node CREATED="1456159691398" ID="ID_1446140659" MODIFIED="1456351386024" TEXT="Security">
<richcontent TYPE="NOTE"><html>
  <head>
    
  </head>
  <body>
    <p>
      Key difference in ours is that we do not store the biometrics anywhere (at the server side).
    </p>
    <p>
      
    </p>
    <p>
      Analyze threats at all components and counter measures.
    </p>
  </body>
</html></richcontent>
</node>
<node CREATED="1456159714200" ID="ID_1694651911" MODIFIED="1456351582327" TEXT="Performance">
<richcontent TYPE="NOTE"><html>
  <head>
    
  </head>
  <body>
    <p>
      We need to check the boundary that preservies uniqueness while achieving repeatability.
    </p>
    <p>
      
    </p>
    <p>
      Computation time.
    </p>
    <p>
      
    </p>
    <p>
      Accuracy - compare with any original mechanism before applying security.
    </p>
    <p>
      
    </p>
    <p>
      Need to find good dataset and good feature extraction mechanism.
    </p>
  </body>
</html></richcontent>
</node>
</node>
<node CREATED="1456351505527" ID="ID_656795243" MODIFIED="1456351603697" POSITION="left" TEXT="Multi Model Biometrics">
<node CREATED="1456351591864" ID="ID_1074983215" MODIFIED="1456351600263" TEXT="Data Sets and Modalities"/>
<node CREATED="1456351608517" ID="ID_365387344" MODIFIED="1456351616080" TEXT="Fusion Criteria"/>
<node CREATED="1456351632211" ID="ID_864242775" MODIFIED="1456351646797" TEXT="How to integrate to ZKP"/>
</node>
</node>
</map>
