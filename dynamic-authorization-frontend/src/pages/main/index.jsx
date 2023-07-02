import MainLayout from "@/pages/main/layout";
import {Descriptions, notification} from "antd";
import {useEffect, useState} from "react";
import axios from "axios";

function Main() {
    const [notificationApi, contextHolder] = notification.useNotification()
    const [username, setUsername] = useState("")
    const [roles, setRoles] = useState([])

    useEffect(() => {
        axios.get(
            "/api/user/role"
        ).then(resp => {
            if (resp.data === null ||
                resp.data.code !== 200 ||
                resp.data.data.filter(role => role.name === 'root').length === 0) {
                notificationApi.warning({
                    message: "information expired, please sign in again"
                })
            } else {
                setUsername(localStorage.getItem("username"))
                const r = []
                resp.data.data.forEach(role => {
                    r.push(role.name)
                })
                setRoles([...r])
            }
        }).catch(err => {
            console.log(err)
        });
    }, [])

    return (
        <>
            {contextHolder}
            <Descriptions title="User Info" bordered>
                <Descriptions.Item label="Username">{username}</Descriptions.Item>
                <Descriptions.Item label="Roles">{roles}</Descriptions.Item>
            </Descriptions>
        </>
    )
}

Main.pageLayout = MainLayout

export default Main