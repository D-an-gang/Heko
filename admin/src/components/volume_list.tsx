import { collection, getDocs, getFirestore } from "firebase/firestore"
import app from "../firebase"
import { useEffect } from "react"

const Volume_list = ({ id }: { id: string }) => {
    const fetch = () => {
        const db = getFirestore(app)
        const ref = collection(db, "books", id, "volume")
        const docs = getDocs(ref)
    }
    useEffect(() => fetch(), [])
    return <></>
}
export default Volume_list