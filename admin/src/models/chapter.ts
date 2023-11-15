import { Timestamp } from "firebase/firestore";

class Chapter {
    id: string;
    book_id: string
    content: string
    create_at: Timestamp
    title: string
    volume_id: string
    constructor(book_id: string, content: string, create_at: Timestamp, id: string, title: string, volume: string) {
        this.id = id;
        this.book_id = book_id
        this.content = content
        this.create_at = create_at
        this.title = title
        this.volume_id = volume
    }
    toString() {
        return `${this.title} - ${this.id} - ${this.volume_id}`
    }
}

export const bookConverter = {
    toFirestore: (book: Chapter) => {
        return {
            id: book.id,
            book_id: book.book_id,
            content: book.content,
            create_at: book.create_at,
            title: book.title,
            volume_id: book.volume_id,
        };
    },
    fromFirestore: (snapshot, options) => {
        const data = snapshot.data(options);
        return new Chapter(data.book_id, data.content, data.create_at, data.id, data.title, data.volume);
    }
};

export default Chapter