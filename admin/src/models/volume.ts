import { Timestamp } from "firebase/firestore"

export class Volume {
    title: string
    cover: string
    id: string
    create_at: Timestamp
    constructor(title: string, cover: string, id: string, create_at: Timestamp) {
        this.title = title
        this.cover = cover
        this.id = id
        this.create_at = create_at
    }
}
export const volumeConverter = {
    toFirestore: (vol: Volume) => {
        return {
            id: vol.id,
            create_at: vol.create_at,
            title: vol.title,
            cover: vol.title,
        };
    },
    fromFirestore: (snapshot, options) => {
        const data = snapshot.data(options);
        return new Volume(data.title, data.cover, data.id, data.create_at);
    }
}