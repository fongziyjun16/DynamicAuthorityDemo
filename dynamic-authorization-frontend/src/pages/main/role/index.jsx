import MainLayout from "@/pages/main/layout";
import {useEffect, useState} from "react";
import {Button, Card, Form, Input, List, Modal, notification, Popconfirm, Space, Table} from "antd";
import axios from "axios";

function RoleIndex() {
    const [loading, setLoading] = useState(false)
    const [notificationApi, contextHolder] = notification.useNotification()
    const [roles, setRoles] = useState([])
    const [isModalOpen, setIsModalOpen] = useState(false);

    const requestData = () => {
        setLoading(true)
        axios.get(
            "/api/role/all"
        ).then(resp => {
            if (resp.data !== null) {
                const {data: dbRoles} = resp.data
                const displayData = []
                dbRoles.forEach(dbRole => {
                    displayData.push({
                        key: dbRole.id,
                        name: dbRole.name
                    })
                })
                // console.log(displayData)
                setRoles([...displayData])
            }
            setLoading(false)
        }).catch(err => {
            console.log(err)
        })
    }

    useEffect(() => {
        requestData()
    }, [])

    const confirmDeleteRole = (record) => {
        setLoading(true)
        axios.delete(
            "/api/role/" + record.name
        ).then(resp => {
            if (resp.data !== null && resp.data.code === 200) {
                requestData()
            } else {
                setLoading(false)
            }
        }).catch(err => {
            console.log(err)
        })
    }

    const addRoleSubmit = (values) => {
        setLoading(true)
        axios.post(
            "/api/role",
            {
                name: values.name
            },
            {
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                }
            }
        ).then(resp => {
            if (resp.data != null) {
                const {code, message} = resp.data
                if (code === 200) {
                    notificationApi.success({
                        message: "Add New Role Successfully",
                    });
                    setIsModalOpen(false)
                    requestData()
                } else {
                    notificationApi.warning({
                        message: message
                    })
                }
            }
            setLoading(false)
        }).catch(err => {
            console.log(err)
            setLoading(false)
        })
    }

    return (
        <>
            {contextHolder}
            <Card
                title="Role List"
                extra={<Button type="primary" onClick={() => setIsModalOpen(true)}>Create</Button>}
            >
                <Table
                    columns={[
                        {
                            title: "Name",
                            dataIndex: "name",
                            key: "name",
                        },
                        {
                            title: "Action",
                            key: "action",
                            render: (_, record) => (
                                <Space>
                                    <Popconfirm
                                        title="Delete the role"
                                        description="Are you sure to delete this role?"
                                        onConfirm={() => confirmDeleteRole(record)}
                                        okText="Yes"
                                        cancelText="No"
                                    >
                                        <Button danger>Delete</Button>
                                    </Popconfirm>
                                </Space>
                            )
                        }
                    ]}
                    dataSource={roles}
                    loading={loading}
                />
            </Card>
            <Modal
                title="Create Form"
                open={isModalOpen}
                footer={null}
                onCancel={() => setIsModalOpen(false)}
                destroyOnClose={true}
            >
                <Form
                    name="addRoleForm"
                    labelCol={{
                        span: 8,
                    }}
                    wrapperCol={{
                        span: 16,
                    }}
                    onFinish={addRoleSubmit}
                    autoComplete="off"
                >
                    <Form.Item
                        label="Role Name"
                        name="name"
                        rules={[
                            {
                                required: true,
                                message: 'Please input role name!',
                            },
                        ]}
                    >
                        <Input/>
                    </Form.Item>

                    <Form.Item
                        wrapperCol={{
                            offset: 8,
                            span: 16,
                        }}
                    >
                        <Button type="primary" htmlType="submit" disabled={loading}>
                            Submit
                        </Button>
                    </Form.Item>
                </Form>
            </Modal>
        </>
    )
}

RoleIndex.pageLayout = MainLayout

export default RoleIndex;