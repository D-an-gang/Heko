import { collection, getDocs, getFirestore } from "firebase/firestore";
import { useEffect, useState } from "react";
import app from "../firebase";
import { User, userConverter } from "../models/user";
import UserTable from "../components/user_table";
import Loading from "../components/loading";

const user_list: User[] = []
const Users = () => {
  const db = getFirestore(app);
  const [isLoading, setLoading] = useState(false)
  useEffect(() => { fetch() }, []);
  const fetch = async () => {
    setLoading(true)
    const querySnapshot = await getDocs(collection(db, "users").withConverter(userConverter));
    querySnapshot.forEach((doc) => {
      // doc.data() is never undefined for query doc snapshot
      const user = doc.data()
      user_list.push(user)
    });
    setLoading(false)
  }
  if (isLoading) {
    return <Loading />
  }
  return <div>
    <h1>User list</h1>
    <UserTable data={user_list} />
  </div>
}
export default Users
