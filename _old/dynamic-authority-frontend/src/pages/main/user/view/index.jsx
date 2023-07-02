import MainLayout from "@/pages/main/layout";
import {useEffect, useState} from "react";
import axios from "axios";
import {Button, Card, Modal, Space, Table, Tag} from "antd";

function ViewUser() {
    const [users, setUsers] = useState([])
    const [isModalOpen, setIsModalOpen] = useState(false)
    const [roles, setRoles] = useState([])
    const [authorities, setAuthorities] = useState([])

    useEffect(() => {
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
    }, [])

    const userDetailDisplay = (record) => {
        setRoles([...record.roleNames])
        setAuthorities([...record.authorityNames])
        setIsModalOpen(true)
    }

    return (
        <>
            <Card title="User Info">
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
                                <Button type="primary" onClick={() => userDetailDisplay(record)}>
                                    Detail
                                </Button>
                            )
                        }
                    ]}
                    dataSource={users}
                />
            </Card>
            <Modal
                title="User Detail"
                open={isModalOpen}
                onCancel={() => setIsModalOpen(false)}
                footer={null}
                destroyOnClose={true}
            >
                <Space direction="vertical" style={{width: "100%"}}>
                    <Card title="Role Info">
                        {
                            roles.map(role => (
                                <Tag key={role}>
                                    {role}
                                </Tag>
                            ))
                        }
                    </Card>
                    <Card title="Authority Info">
                        {
                            authorities.map(authority => (
                                <Tag key={authority}>
                                    {authority}
                                </Tag>
                            ))
                        }
                    </Card>
                </Space>
            </Modal>
        </>
    )
}

ViewUser.pageLayout = MainLayout

export default ViewUser