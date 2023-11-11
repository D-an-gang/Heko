export class User {
    id: string
    username: string
    email: string
    imgUrl: string
    constructor(email: string, id: string, imgUrl: string, username: string) {
        this.id = id
        this.username = username;
        this.email = email;
        this.imgUrl = imgUrl;
    }
    toString() {
        return this.id + ', ' + this.username + ', ' + this.email + this.imgUrl;
    }
}

// Firestore data converter
export const userConverter = {
    toFirestore: (user: User) => {
        return {
            email: user.email,
            id: user.id,
            imgUrl: user.imgUrl,
            username: user.username,
        };
    },
    fromFirestore: (snapshot, options) => {
        const data = snapshot.data(options);
        return new User(data.email, data.id, data.imgUrl, data.username);
    }
};