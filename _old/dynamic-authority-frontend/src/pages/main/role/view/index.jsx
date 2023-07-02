import MainLayout from "@/pages/main/layout";
import {useEffect, useState} from "react";
import {Button, Card, List, Modal, Popconfirm, Space, Table} from "antd";
import axios from "axios";

function ViewRole() {

    const [loading, setLoading] = useState(false)
    const [roles, setRoles] = useState([])
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [authorities, setAuthorities] = useState([])

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
            setLoading(false)
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
            setLoading(false)
        })
    }

    const openDetail = (record) => {
        setIsModalOpen(true)
        axios.get(
            "/api/role/authority/" + record.name
        ).then(resp => {
            if (resp.data !== null) {
                // console.log(resp.data.data)
                // setAuthorities([{name: "aaa"}])
                setAuthorities([...resp.data.data])
            }
        }).catch(err => {
            console.log(err)
        })
    }

    return (
        <>
            <Card title="Role List">
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
                                    <Button type="primary" onClick={() => openDetail(record)}>Detail</Button>
                                </Space>
                            )
                        }
                    ]}
                    dataSource={roles}
                    loading={loading}
                />
            </Card>
            <Modal
                title="Authority List"
                open={isModalOpen}
                footer={null}
                onCancel={() => setIsModalOpen(false)}
                destroyOnClose={true}
            >
                <List
                    bordered
                    dataSource={authorities}
                    renderItem={(item) => <List.Item>{item.name}</List.Item>}
                />
            </Modal>
        </>
    )
}

ViewRole.pageLayout = MainLayout

export default ViewRole