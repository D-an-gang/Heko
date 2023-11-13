import { collection, getDocs, getFirestore } from "firebase/firestore";
import { useEffect } from "react";
import app from "../firebase";
import { User, userConverter } from "../models/user";
import UserTable from "../components/user_table";
interface Users {
  id: string
  username: string
  email: string
  imgUrl: string
}

const Users = () => {
  const db = getFirestore(app);

  const user_list: User[] = []
  useEffect(() => {
    const fetch = async () => {
      const querySnapshot = await getDocs(collection(db, "users").withConverter(userConverter));
      querySnapshot.forEach((doc) => {
        // doc.data() is never undefined for query doc snapshot

        const user = doc.data()
        user_list.push(user)
      });
    }
    fetch()
    return () => {
      // Clean up the subscription
    };
  });
  return <div>
    <h1>User list</h1>
    <UserTable data={user_list} />
  </div>
}
export default Users
