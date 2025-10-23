# Student Tracker

A Kotlin-based Android tablet application for tracking student sign-ins and sign-outs in educational settings. Built with modern Android development practices and Supabase backend integration.

## Features

- **Student Management**: Track student attendance with real-time sign-in/sign-out functionality
- **Payment Tracking**: Monitor payment status with overdue payment highlighting
- **Real-time Updates**: Live synchronization using Supabase realtime capabilities
- **User Authentication**: Secure login system
- **History & Reporting**: Comprehensive payment and attendance history with filtering
- **Family Management**: Edit family information and student details
- **Responsive UI**: Optimized for tablet devices with Jetpack Compose

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Backend**: Supabase (PostgreSQL + Realtime)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Authentication**: Supabase Auth

## Installation

1. Clone the repository:
```bash
git clone https://github.com/your-username/student-tracker.git
```

2. Open the project in Android Studio

3. Configure Supabase:
   - Create a Supabase project
   - Set up your database schema
   - Add your Supabase URL and anon key to `local.properties`:
   ```
   SUPABASE_URL=your_supabase_url
   SUPABASE_ANON_KEY=your_supabase_anon_key
   ```

4. Build and run the application on an Android tablet or emulator

## Usage

### Sign In/Out
- Search for students by name
- Tap to sign in or out
- Real-time updates across all devices

### Payment Management
- View payment status for each student
- Confirm payments
- Track overdue payments with visual indicators

### Student Management
- Add new students and families
- Edit existing student information
- Manage class assignments

### History
- Filter attendance and payment history
- View comprehensive records

## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues for bugs and feature requests.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Development Team

- **Shawarm0**
- **ama6erry**

---

For questions or support, please open an issue in the GitHub repository.
