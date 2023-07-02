import MainLayout from "@/pages/main/layout";
import {useEffect, useState} from "react";
import axios from "axios";
import {Button, Card, Form, Input, Modal, notification, Space, Table, Tag} from "antd";
import {error} from "next/dist/build/output/log";
import RoleSelector from "@/pages/main/component/RoleSelector";
import {base} from "next/dist/build/webpack/config/blocks/base";

function UserIndex() {
    const [notificationApi, contextHolder] = notification.useNotification()
    const [users, setUsers] = useState([])
    const [isModalOpen, setIsModalOpen] = useState(false)
    const [modalTitle, setModalTitle] = useState("")
    const [modalMode, setModalMode] = useState("")
    const [roles, setRoles] = useState([])
    const [selectedRecord, setSelectedRecord] = useState()
    const [selectedRoles, setSelectedRoles] = useState([])

    useEffect(() => {
        requestData()
    }, [])

    const requestData = () => {
        axios.get(
            "/api/user/all"
        ).then(resp => {
            if (resp.data !== null) {
                const {data: dbUsers} = resp.data
                const u = []
                dbUsers.forEach(dbUser => {
                    u.push({
                        key: dbUser.username,
                        ...dbUser
                    })
                })
                setUsers([...u])
            }
        }).catch(err => {
            console.log(err)
        })
    }

    const modeChoice = () => {
        if (modalMode === "detail") {
            return (
                roles.map(role => (
                    <Tag key={role}>
                        {role}
                    </Tag>
                ))
            )
        } else if (modalMode === "signUp") {
            return (
                <>
                    <Form
                        name="signUpForm"
                        labelCol={{
                            span: 8,
                        }}
                        wrapperCol={{
                            span: 16,
                        }}
                        onFinish={handleSignUp}
                        autoComplete="off"
                    >
                        <Form.Item
                            label="Username"
                            name="username"
                            rules={[
                                {
                                    required: true,
                                    message: 'Please input your username!',
                                },
                            ]}
                        >
                            <Input />
                        </Form.Item>

                        <Form.Item
                            label="Password"
                            name="password"
                            rules={[
                                {
                                    required: true,
                                    message: 'Please input your password!',
                                },
                            ]}
                        >
                            <Input.Password />
                        </Form.Item>

                        <Form.Item
                            label="Confirm Password"
                            name="confirmPassword"
                            rules={[
                                {
                                    required: true,
                                    message: 'Please input your password again!',
                                },
                            ]}
                        >
                            <Input.Password />
                        </Form.Item>

                        <Form.Item
                            wrapperCol={{
                                offset: 8,
                                span: 16,
                            }}
                        >
                            <Button type="primary" htmlType="submit">
                                Submit
                            </Button>
                        </Form.Item>
                    </Form>
                </>
            )
        } else { // modalMode === "roles update"
            return (
                <>
                    <Space direction="vertical" style={{width: "100%"}}>
                        <RoleSelector
                            selectedItems={selectedRoles}
                            selectionChange={rolesChange}
                        />
                        <Button type="primary" onClick={() => roleUpdate()}>Submit</Button>
                    </Space>
                </>
            )
        }
    }

    const userDetailDisplay = (record) => {
        setRoles([...record.roles.map(role => role.name)])
        setModalMode("detail")
        setModalTitle("Role Details")
        setIsModalOpen(true)
    }

    const userSignUp = () => {
        setModalMode("signUp")
        setModalTitle("Sign Up")
        setIsModalOpen(true)
    }

    const handleSignUp = (values) => {
        const {username, password, confirmPassword} = values
        if (password !== confirmPassword) {
            notificationApi.error({
                message: "Password is not matched with Confirm Password"
            })
        } else {
            axios.post(
                "/api/user/sign-up",
                {
                    username: username,
                    password: password
                },
                {
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    }
                }
            ).then(resp => {
                if (resp.data !== null) {
                    const {code, messsage} = resp.data
                    if (code === 200) {
                        requestData()
                        setIsModalOpen(false)
                        notificationApi.success({
                            message: 'Sign Up Successfully'
                        })
                    } else {
                        notificationApi.error({
                            message: messsage
                        })
                    }
                } else {
                    notificationApi.warning({
                        message: 'try again later'
                    })
                }
            }).catch(err => {
                console.log(err)
            })
        }
    }

    const userUpdate = (record) => {
        setSelectedRecord(record)
        setSelectedRoles([...record.roles.map(role => role.name)])
        setModalMode("userUpdate")
        setModalTitle("Sign Up")
        setModalTitle("Roles Update")
        setIsModalOpen(true)
    }

    const rolesChange = (values) => {
        setSelectedRoles([...values])
    }

    const roleUpdate = () => {
        axios.post(
            "/api/user/role",
            {
                username: selectedRecord.username,
                roleNames: selectedRoles
            },
            {
                headers: {
                    "Content-Type": "application/json"
                }
            }
        ).then(resp => {
            if (resp.data !== null) {
                const {code, message} = resp.data
                if (code === 200) {
                    setIsModalOpen(false)
                    requestData()
                    notificationApi.success({
                        message: "Update Successfully"
                    })
                } else {
                    notificationApi.error({
                        message: message
                    })
                }
            } else {
                notificationApi.warning({
                    message: "try again later"
                })
            }
        }).catch(err => {
            console.log(err)
        })
    }

    return (
        <>
            {contextHolder}
            <Card
                title="User Info"
                extra={<Button onClick={() => userSignUp()}>Sign Up</Button>}
            >
                <Table
                    columns={[
                        {
                            title: 'Username',
                            dataIndex: 'username',
                            key: 'username',
                        },
                        {
                            title: 'Action',
                            dataIndex: 'action',
                            key: 'action',
                            render: (_, record) => (
                                <Space>
                                    <Button onClick={() => userDetailDisplay(record)}>
                                        Detail
                                    </Button>
                                    <Button onClick={() => userUpdate(record)}>
                                        Update
                                    </Button>
                                </Space>
                            )
                        }
                    ]}
                    dataSource={users}
                />
            </Card>
            <Modal
                title={modalTitle}
                open={isModalOpen}
                onCancel={() => setIsModalOpen(false)}
                footer={null}
                destroyOnClose={true}
            >
                {modeChoice()}
            </Modal>
        </>
    )
}

UserIndex.pageLayout = MainLayout

export default UserIndex