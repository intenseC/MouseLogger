# MouseLogger
Android APP to track and log USB mouse.
Logs are printed @ http://127.0.0.1:8888/coords


//////////////////////////////////////////////////

Installation and running:

//////////////////////////////////////////////////


Install Android Studio:

Go to the Android Studio website: https://developer.android.com/studio
Click on the "Download" button and choose the appropriate version for your operating system (Windows, macOS, or Linux).
Follow the installation instructions provided by the Android Studio installer.
Clone the MouseLogger repository from GitHub:

Open a terminal or command prompt.
Navigate to the directory where you want to clone the repository.
Run the following command to clone the repository:


git clone https://github.com/intenseC/MouseLogger.git

Replace "your-username" with your actual GitHub username.

Import the project in Android Studio:

Open Android Studio.
Click on "Open an existing Android Studio project" or go to "File" > "Open" and navigate to the cloned MouseLogger directory.
Select the "MouseLogger" directory and click "OK".
Wait for Android Studio to import the project. It may take a few moments.
Configure the MouseLogger project:

If the project's Gradle files need to be synced, Android Studio will prompt you to do so. Click on "Sync Now" to sync the project.
Once the project is synced, go to the "Project" view in the Android Studio sidebar.
Open the "app" directory and locate the "local.properties" file.
Open the "local.properties" file and add the following line:

sdk.dir=/path/to/your/Android/sdk

Replace "/path/to/your/Android/sdk" with the actual path to your Android SDK installation directory.

Build and run the MouseLogger app:

Make sure you have an Android device connected to your computer, as Android Virtual Device in Android Studio has no means to work with USB mouse.
In the Android Studio toolbar, select the target device from the device dropdown menu.
Click on the "Run" button (green triangle) or go to "Run" > "Run 'app'" to build and run the MouseLogger app on the selected device.
Android Studio will compile the project, install the app on the device, and launch it.
That's it! The MouseLogger app should now be running on your Android device or emulator. You can interact with the app and test its functionality.

Note: If you encounter any issues during the installation or running of the MouseLogger project,
make sure to check the project's documentation or consult the project's README file for any specific instructions or troubleshooting steps.


//////////////////////////////////////////////////


Guide on fetching an APK file from GitHub and installing it on an Android phone:


//////////////////////////////////////////////////


Find the MouseLogger APK file on GitHub:

Go to the MouseLogger GitHub repository page: https://github.com/your-username/MouseLogger
Navigate to the "Releases" section or look for the latest release.
Locate the MouseLogger APK file you want to download. It should be listed under the release assets.

Download the MouseLogger APK file:

Click on the MouseLogger APK file link to start the download.
Save the APK file to a convenient location on your computer.

Enable installation from unknown sources on your Android phone:

On your Android phone, go to "Settings".
Navigate to "Security" or "Privacy" (the exact location may vary depending on your device).
Look for the option "Unknown sources" or "Install unknown apps".
Toggle the switch to enable installation from unknown sources.

Transfer the MouseLogger APK file to your Android phone:

Connect your Android phone to your computer using a USB cable.
On your computer, copy the MouseLogger APK file to your phone's storage.
You can place it in the Downloads folder or any other location.

Install the MouseLogger APK on your Android phone:

On your Android phone, open a file manager app.
Navigate to the location where you transferred the MouseLogger APK file.
Tap on the APK file to start the installation.
Follow the on-screen prompts to complete the installation.

Grant necessary permissions:

After the installation is complete, open the MouseLogger app.
Grant any necessary permissions that the app requires, such as access to storage, network, or location.
Follow the prompts on the screen to allow the permissions.

Launch and use the MouseLogger app:

Once the app is installed and permissions are granted, you can launch the MouseLogger app from your app drawer or home screen.
Follow any instructions or setup steps provided by the app to configure it.
And that's it! You have successfully retrieved the MouseLogger APK from GitHub and installed it on your Android phone.
You can now use the app to track mouse coordinates and to log them.


//////////////////////////////////////////////////
