---

# TestNest

A comprehensive online examination platform built with Java and Spring Boot. TestNest allows universities, faculties, and students to manage, create, and participate in online exams securely and efficiently.

## Features

- **User Roles:** Admin, Faculty, Student
- **Authentication:** Secure login and registration with JWT tokens
- **Exam Management:** Faculty can create, schedule, and manage exams
- **Question Management:** Add single or multiple questions to exams
- **Exam Participation:** Students can view, attempt, and submit exams
- **Results and Leaderboard:** Instant scoring and leaderboard generation
- **University Management:** Admins can add and list universities

## API Endpoints

### Authentication

- `POST /api/auth/register` — Register a new user
- `POST /api/auth/login` — Login and receive access tokens

### Admin

- `GET /admin/hello` — Admin test endpoint
- `POST /admin/university/add` — Add universities
- `GET /admin/university/all` — List all universities

### Faculty

- `POST /faculty/exam/create` — Create an exam
- `GET /faculty/exam/my` — List faculty exams
- `GET /faculty/university` — List all exams in faculty’s university
- `POST /faculty/exam/question/add/{examId}` — Add question to exam
- `POST /faculty/exam/question/add-multiple/{examId}` — Add multiple questions
- `GET /faculty/exam/question/{examId}` — Get questions for an exam

### Student

- `GET /student/hey` — Student test endpoint
- `GET /student/exams/live` — List live exams for student
- `GET /student/exams/{examId}/start` — Start an exam
- `POST /student/exams/{examId}/submit` — Submit answers

### General

- `GET /register` — Registration page
- `GET /login` — Login page
- `GET /exams/{examId}/leaderboard` — Exam leaderboard

## Data Models

Key response DTOs include:

- **LoginResponse**: `accessToken`, `refreshToken`, `role`
- **ExamResponseDto**: `id`, `title`, `subject`, `startTime`, `endTime`, `durationInMinutes`, `createdBy`, `universityName`, `status`
- **SubmitExamResponse**: `totalQuestions`, `correctAnswers`, `score`, `attempted`, `skipped`, `skippedQuestionIds`
- **LeaderboardEntryDto**: (fields may include user and score info)
- **Question/QuestionViewDto/University**: See class definitions

## Getting Started

### Prerequisites

- Java 17+
- Maven

### Setup

```bash
git clone https://github.com/Subh-0-Dev/TestNest.git
cd TestNest
mvn clean install
```

### Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

## Contributing

Contributions are welcome! Please fork the repo and submit a pull request.

## License

[ I Don't Know, What to put here ]

---
