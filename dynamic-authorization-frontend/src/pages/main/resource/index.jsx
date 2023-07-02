import MainLayout from "@/pages/main/layout";
import {Button, Card, Modal, notification, Select, Space, Table, Tag} from "antd";
import {useEffect, useState} from "react";
import axios from "axios";
import RoleSelector from "@/pages/main/component/RoleSelector";
import ResourceAuthTypeSelector from "@/pages/main/component/ResourceAuthTypeSelector";

function ResourceIndex() {
    const [notificationApi, contextHolder] = notification.useNotification()
    const [resources, setResources] = useState([])
    const [modalMode, setModalMode] = useState("")
    const [isModalOpen, setIsModalOpen] = useState(false)
    const [roles, setRoles] = useState([])
    const [selectedRecord, setSelectedRecord] = useState()
    const [resourceAuthType, setResourceAuthType] = useState("")
    const [resourceRoles, setResourceRoles] = useState([])

    useEffect(() => {
        requestResourceData()
    }, [])

    const requestResourceData = () => {
        axios.get(
            "/api/resource/all"
        ).then(resp => {
            if (resp.data !== null) {
                const {data: dbResources} = resp.data
                const r = []
                dbResources.map(dbResource => {
                    r.push({
                        key: dbResource.method + ":" + dbResource.path,
                        ...dbResource
                    })
                })
                setResources([...r])
            }
        }).catch(err => {
            console.log(err)
        })
    }

    const requestResourceDetail = (record) => {
        setModalMode("roleDetails")
        setRoles([...record.roles])
        setIsModalOpen(true)
    }

    const updateRoles = (record) => {
        console.log(record.roles.map(role => role.name))
        setSelectedRecord(record)
        setModalMode("updateRoles")
        setResourceAuthType(record.authType)
        setResourceRoles([...record.roles.map(role => role.name)])
        setIsModalOpen(true)
    }

    const selectedRolesChange = (values) => {
        setResourceRoles([...values])
    }

    const updateResource = () => {
        // console.log(resourceRoles)
        axios.post(
            "/api/resource/role",
            {
                method: selectedRecord.method,
                path: selectedRecord.path,
                authType: resourceAuthType,
                roleNames: resourceRoles
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
                    notificationApi.success({
                        message: 'Update Successfully'
                    })
                    requestResourceData()
                }  else {
                    notificationApi.warning({
                        message: message
                    })
                }
            }
        }).then(err => {
            console.log(err)
        })
    }

    return (
        <>
            {contextHolder}
            <Card title="Resrouce Info">
                <Table
                    columns={[
                        {
                            title: 'Method',
                            dataIndex: 'method',
                            key: 'method',
                        },
                        {
                            title: 'Path',
                            dataIndex: 'path',
                            key: 'path',
                        },
                        {
                            title: 'Authorization Type',
                            dataIndex: 'authType',
                            key: 'authType',
                        },
                        {
                            title: 'Action',
                            dataIndex: 'action',
                            key: 'action',
                            render: (_, record) => (
                                <Space>
                                    <Button onClick={() => requestResourceDetail(record)}>
                                        Role Detail
                                    </Button>
                                    <Button onClick={() => updateRoles(record)}>
                                        Update
                                    </Button>
                                </Space>
                            )
                        }
                    ]}
                    dataSource={resources}
                />
            </Card>
            <Modal
                title={modalMode === "roleDetails" ? "Role Details" : "Update Role"}
                open={isModalOpen}
                onCancel={() => setIsModalOpen(false)}
                footer={null}
                destroyOnClose={true}
            >
                <>
                    {
                        modalMode === "roleDetails" ?
                            (roles.length > 0 ? roles.map(role => (
                                <Tag key={role.name}>
                                    {role.name}
                                </Tag>
                            )) : <span>No Roles Data</span>) :
                            <>
                                <Space direction="vertical" style={{width: "100%"}}>
                                    <span>Auth Type</span>
                                    <ResourceAuthTypeSelector
                                        selectedItem={resourceAuthType}
                                        selectionChange={(value) => setResourceAuthType(value)}
                                    />
                                    <span>Role</span>
                                    <RoleSelector
                                        selectedItems={resourceRoles}
                                        selectionChange={selectedRolesChange}
                                    />
                                    <Button type="primary" onClick={() => updateResource()}>
                                        Submit
                                    </Button>
                                </Space>
                            </>
                    }
                </>
            </Modal>
        </>
    )
}

ResourceIndex.pageLayout = MainLayout

export default ResourceIndex