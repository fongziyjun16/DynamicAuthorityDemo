import MainLayout from "@/pages/main/layout";
import {useEffect, useState} from "react";
import axios from "axios";
import {Descriptions, List, message} from "antd";
import {useSelector} from "react-redux";
import {selectUsername} from "@/lib/redux/slices/usernameSlice";

function MainIndex() {

    const [messageApi, contextHolder] = message.useMessage();
    const username = useSelector(selectUsername)
    const [roles, setRoles] = useState("")
    const [authorities, setAuthorities] = useState("")

    const errorMessage = (content) => {
        messageApi.open({
            type: "error",
            content: content
        })
    }

    useEffect(() => {
        axios.get(
            "/api/user/authenticate",
            {
                withCredentials: true
            }
        ).then(resp => {
            if (resp.data === null || resp.data.code !== 200) {
                errorMessage("please sign out and sign in again")
            } else {
                axios.get(
                    "/api/user/" + username
                ).then(resp => {
                    if (resp.data !== null) {
                        const {roleNames, authorityNames} = resp.data.data
                        setRoles(roleNames.join("; "))
                        setAuthorities(authorityNames.join("; "))
                    }
                }).catch(err => {
                    console.log(err)
                })
            }
        }).catch(err => {
            console.log(err)
            errorMessage("authenticate failure")
        })
    }, [])

    return (
        <>
            {contextHolder}
            <>
                <Descriptions title="User Info" bordered>
                    <Descriptions.Item label="Username">{username}</Descriptions.Item>
                    <Descriptions.Item label="Roles">{roles}</Descriptions.Item>
                    <Descriptions.Item label="Authorities">{authorities}</Descriptions.Item>
                </Descriptions>
            </>
        </>
    )
}

MainIndex.pageLayout = MainLayout

export default MainIndex