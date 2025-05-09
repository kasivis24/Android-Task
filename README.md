# Media Storage App (Offline & Online) (Zpace)

## Overview
This is a media storage app that allows users to store, manage, and retrieve files both online and offline. It integrates Firebase for authentication and cloud storage, while also offering local storage support. The app includes features like folder creation, file upload/download, theme switching, and advanced search options.

## Features

### **Authentication**
- Firebase Email and Password Authentication for secure login/logout.

### **Dashboard Page**
- Create folders and store them in the cloud.
- Logout functionality.
- View storage file size using a chart.
- Toggle between dark and light mode using SharedPreferences.
- Filter option for searching folders.

### **Media Gallery Page**
- Displays a list of folders and their associated files.
- View file details including name and information.
- Copy single files or entire folders.
- Delete single files or entire folders.

### **Upload Page**
- Upload and download files seamlessly.
- Uses Firebase Cloud Storage and Firestore for file management.

## Libraries Used
- **Firebase Authentication** - For email/password-based authentication.
- **Firebase Cloud Storage** - For storing uploaded files.
- **Firebase Firestore** - For managing file metadata.
- **SharedPreferences** - For storing theme preferences (dark/light mode).
- **Chart Library** - For visualizing storage usage.

## How to Use
1. **Sign in** using your email and password.
2. **Create folders** and manage files in the dashboard.
3. **View, copy, and delete files** in the media gallery.
4. **Upload or download files** from the cloud in the upload page.
5. **Switch themes** between dark and light mode in settings.
6. **Use filters** to quickly find folders and files.

## Installation
1. Clone the repository:
   ```sh
   git clone [https://github.com/kasivis24/Android-Task]
   ```
2. Apk for Download:
   ```sh
   git clone [https://github.com/kasivis24/Android-Task/blob/master/app-debug.apk]
   ```
2. Open the project in your preferred IDE (Android Studio/VS Code).
3. Add your Firebase configuration file (`google-services.json`).
4. Run the app on an emulator or physical device.

## ScreenShots
![IMG-20250331-WA0011](https://github.com/user-attachments/assets/5a050ebb-b96b-4dd9-99a9-4dfee6c1b92a)
![IMG-20250331-WA0009](https://github.com/user-attachments/assets/9b009045-17fb-47ac-bd49-6d2b005d9085)
![IMG-20250331-WA0010](https://github.com/user-attachments/assets/f89733fb-442d-4e30-93ce-227a1f40fade)
![IMG-20250331-WA0006](https://github.com/user-attachments/assets/29d679eb-c476-4cca-9a22-1e1edfac305b)
![IMG-20250331-WA0007](https://github.com/user-attachments/assets/943176fb-9baa-4a4b-8c10-b46816cf785c)
![IMG-20250331-WA0008](https://github.com/user-attachments/assets/2ee31784-3233-4bc8-ab95-e28acdebebca)
![IMG-20250331-WA0004](https://github.com/user-attachments/assets/67eed738-3838-4c11-bfb0-e8a3acc273ef)
![IMG-20250331-WA0005](https://github.com/user-attachments/assets/a543fa68-1feb-42fb-9a22-9cb9674c06a7)



## Future Enhancements
- Add offline storage sync for better user experience.
- Implement additional file-sharing options.
- Enhance search functionality with AI-based recommendations.

## License
This project is open-source and available for use under the MIT License.

---

Feel free to customize this README further based on additional features or changes in the project!

