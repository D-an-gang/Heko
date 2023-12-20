import { Timestamp } from "firebase/firestore";

class Chapter {
    id: string;
    content: string
    create_at: Timestamp
    title: string
    constructor(content: string, create_at: Timestamp, id: string, title: string) {
        this.id = id;
        this.content = content
        this.create_at = create_at
        this.title = title
    }
}

export const chapterConverter = {
    toFirestore: (book: Chapter) => {
        return {
            id: book.id,
            content: book.content,
            create_at: book.create_at,
            title: book.title,
        };
    },
    fromFirestore: (snapshot, options) => {
        const data = snapshot.data(options);
        return new Chapter(data.content, data.create_at, data.id, data.title);
    }
};

export default Chapter