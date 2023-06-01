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
