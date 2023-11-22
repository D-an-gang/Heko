import { Timestamp } from "firebase/firestore"

export class Book {
    id: string
    title: string
    author: string
    chapters_number: number
    last_update: Timestamp
    book_cover: string
    description: string
    genre: string[]
    create_at: Timestamp
    constructor(author: string, book_cover: string, chapters_number: number, created_at: Timestamp,
        description: string, genre: string, id: string, last_update: Timestamp, title: string
    ) {


        this.id = id
        this.author = author
        this.book_cover = book_cover
        this.chapters_number = chapters_number
        this.create_at = created_at
        this.description = description
        this.genre = genre.split(";")
        this.last_update = last_update
        this.title = title
    }
    toString() {
        return `${this.author} - ${this.title} - ${this.id}`
    }
}

// Firestore data converter
export const bookConverter = {
    toFirestore: (book: Book) => {
        return {
            author: book.author,
            book_cover: book.book_cover,
            chapters_number: book.chapters_number,
            create_at: book.create_at,
            description: book.description,
            genre: book.genre.join(";"),
            id: book.id,
            last_update: book.last_update,
            title: book.title,
        };
    },
    fromFirestore: (snapshot, options) => {
        const data = snapshot.data(options);
        return new Book(data.author, data.book_cover, data.chapters_number, data.created_at, data.description, data.genre, data.id, data.last_update, data.title
        );
    }
};