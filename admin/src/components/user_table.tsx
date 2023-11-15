import { CSSProperties } from "react"
import { User } from "../models/user"

const UserTable = ({ data }: { data: User[] }) => {
    return <>
        <table className="table table-hover" style={style}>
            <thead>
                <tr>
                    <th scope="col">STT</th>
                    <th scope="col">Email</th>
                    <th scope="col">Username</th>
                    <th scope="col">Action</th>
                </tr>
            </thead>
            <tbody>{
                data.map((item: User) =>
                    <tr key={item.id}>
                        <td className="counterCell"></td>
                        <td>{item.email}</td>
                        <td>{item.username}</td>
                        <td><button type="button" className="btn btn-danger">Delete User</button></td>
                    </tr>
                )
            }
            </tbody>
        </table>
    </>

}
export default UserTable

const style:CSSProperties={
    textAlign: "left" 
}