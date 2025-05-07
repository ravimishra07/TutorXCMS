import React from "react";
import { User as FirebaseUser } from "firebase/auth";
import {
    Authenticator,
    buildCollection,
    buildSchema,
    FirebaseCMSApp,
    NavigationBuilder,
    NavigationBuilderProps
} from "@camberi/firecms";

import "typeface-rubik";
import "typeface-space-mono";

const firebaseConfig = {
    apiKey: "AIzaSyD9hlDz-1LGrWAULK1ZkGsdxPEnRG-x9qg",
    authDomain: "projectsam-9d34e.firebaseapp.com",
    databaseURL: "https://projectsam-9d34e-default-rtdb.firebaseio.com",
    projectId: "projectsam-9d34e",
    storageBucket: "projectsam-9d34e.firebasestorage.app",
    messagingSenderId: "270412335979",
    appId: "1:270412335979:web:2600bdb3b83f8044bb9a2e",
    measurementId: "G-EYXTJLHNL9"
};

const locales = {
    "en-US": "English (United States)",
    "es-ES": "Spanish (Spain)",
    "de-DE": "German"
};

type StudentProfile = {
    student_id: string;
    name: string;
    class: number;
    board: string;
    course: any; // Firestore reference
    location?: string;
    mob: string;
    email: string;
    stream: string;
    subjects: string[];
    assigned_tutor: string;
    admission_date: Date;
    notes: string;
    is_active: boolean;
    progress: {
        [subject_code: string]: {
            [topic: string]: boolean;
        };
    };
};

type Course = {
    id: string;
    board: "CBSE" | "ICSE" | "State";
    class: number;
    syllabus: {
        id: string;
        subject: string;
        chapters: string[];
    }[];
};

const studentProfileSchema = buildSchema<StudentProfile>({
    name: "Students",
    properties: {
        student_id: { title: "Student ID", validation: { required: true }, dataType: "string" },
        name: { title: "Full Name", validation: { required: true }, dataType: "string" },
        class: { title: "Class", validation: { required: true }, dataType: "number" },
        email: { title: "Parent Email", validation: { required: true }, dataType: "string" },
        mob: { title: "Mobile Number", validation: { required: true }, dataType: "string" },
        course: {
            title: "Course",
            validation: { required: true },
            dataType: "reference",          // 🔁 reference to another collection
            path: "courses",                // 🔗 the linked collection
            previewProperties: ["id", "board", "class"]
        },
        board: {
            title: "Board",
            dataType: "string",
            config: {
                enumValues: { CBSE: "CBSE", ICSE: "ICSE", State: "State Board" }
            }
        },
        stream: {
            title: "Stream",
            dataType: "string",
            config: {
                enumValues: { Science: "Science", Commerce: "Commerce", Arts: "Arts" }
            }
        },
        subjects: {
            title: "Subjects",
            dataType: "array",
            of: { dataType: "string" }
        },
        assigned_tutor: { title: "Assigned Tutor", dataType: "string" },
        admission_date: { title: "Admission Date", dataType: "timestamp" },
        notes: { title: "Notes", dataType: "string" },
        is_active: { title: "Is Active", dataType: "boolean" },
        progress: {
            title: "Progress",
            dataType: "map",
            properties: {
                subject_code: {
                    dataType: "map",
                    properties: {
                        topic: { dataType: "boolean" }
                    }
                }
            }
        }
    }
});
export type TutorProfile = {
    tutor_id: string;
    name: string;
    location: string;
    courses: string[]; // Array of course references
    subjects_taught: string[];
    fee_range: string;
    qualification: string;
    student_ids: string[];
    is_active: boolean;
    is_verified: boolean;
};

const courseSchema = buildSchema<Course>({
    name: "Courses",
    properties: {
        id: { title: "ID", validation: { required: true }, dataType: "string" },
        board: {
            title: "Board",
            validation: { required: true },
            dataType: "string",
            config: {
                enumValues: { CBSE: "CBSE", ICSE: "ICSE", State: "State Board" }
            }
        },
        class: { title: "Class", validation: { required: true }, dataType: "number" },
        syllabus: {
            title: "Syllabus",
            validation: { required: true },
            dataType: "array",
            of: {
                dataType: "map",
                properties: {
                    id: { title: "ID", validation: { required: true }, dataType: "string" },
                    subject: { title: "Subject", validation: { required: true }, dataType: "string" },
                    chapters: {
                        title: "Chapters",
                        validation: { required: true },
                        dataType: "array",
                        of: { dataType: "string" }
                    }
                }
            }
        }
    }
});

export const tutorProfileSchema = buildSchema<TutorProfile>({
    name: "Tutors",
    properties: {
        tutor_id: {
            title: "Tutor ID",
            validation: { required: true },
            dataType: "string"
        },
        name: {
            title: "Full Name",
            validation: { required: true },
            dataType: "string"
        },
        location: {
            title: "Location",
            dataType: "string"
        },
        courses: {
            title: "Courses",
            validation: { required: true },
            dataType: "array",
            of: {
                dataType: "reference",
                path: "courses",
                previewProperties: ["id", "board", "class"]
            }
        },
        subjects_taught: {
            title: "Subjects Taught",
            dataType: "array",
            of: { dataType: "string" }
        },
        fee_range: {
            title: "Fee Range",
            dataType: "string"
        },
        qualification: {
            title: "Qualification",
            dataType: "string"
        },
        student_ids: {
            title: "Student IDs",
            dataType: "array",
            of: { dataType: "string" }
        },
        is_active: {
            title: "Is Active",
            dataType: "boolean"
        },
        is_verified: {
            title: "Is Verified",
            dataType: "boolean"
        }
    }
});

export default function App() {
    const navigation: NavigationBuilder = async ({ user, authController }: NavigationBuilderProps) => {
        return {
            collections: [
                buildCollection({
                    path: "students",
                    schema: studentProfileSchema,
                    name: "Students",
                    permissions: ({ authController }) => ({
                        edit: true,
                        create: true,
                        delete: authController.extra.roles.includes("admin")
                    })
                }),
                buildCollection({
                    path: "courses",
                    schema: courseSchema,
                    name: "Courses",
                    permissions: ({ authController }) => ({
                        edit: true,
                        create: true,
                        delete: authController.extra.roles.includes("admin")
                    })
                }),
                buildCollection({
                    path: "tutors",
                    schema: tutorProfileSchema,
                    name: "Tutors",
                    permissions: ({ authController }) => ({
                        edit: true,
                        create: true,
                        delete: authController.extra.roles.includes("admin")
                    })
                })
            ]
        };
    };

    const myAuthenticator: Authenticator<FirebaseUser> = async ({ user, authController }) => {
        if (user?.email?.includes("flanders")) {
            throw Error("Stupid Flanders!");
        }

        console.log("Allowing access to", user?.email);
        const sampleUserData = await Promise.resolve({ roles: ["admin"] });
        authController.setExtra(sampleUserData);
        return true;
    };

    return (
        <FirebaseCMSApp
            name="Tutor Console"
            authentication={myAuthenticator}
            navigation={navigation}
            firebaseConfig={firebaseConfig}
        />
    );
}