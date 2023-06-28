import MainLayout from "@/pages/main/layout";
import {Button, Card, Modal, Space, Table, Tag} from "antd";
import {useEffect, useState} from "react";
import axios from "axios";

function ViewResource() {

    const [resources, setResources] = useState([])
    const [isModalOpen, setIsModalOpen] = useState(false)
    const [roles, setRoles] = useState([])
    const [authorities, setAuthorities] = useState([])

    useEffect(() => {
        axios.get(
            "/api/resource/endpoints"
        ).then(resp => {
            if (resp.data !== null) {
                const {data: dbResource} = resp.data
                const map = new Map(Object.entries(dbResource))
                const r = []
                map.forEach((paths, method) => {
                    paths.forEach(path => {
                        r.push({
                            key: method + ":" + path,
                            method: method,
                            path: path
                        })
                    })
                })
                // console.log(r)
                setResources([...r])
            }
        }).catch(err => {
            console.log(err)
        })
    }, [])

    const requestResourceDetail = (record) => {
        axios.get(
            "/api/resource?method=" + record.method + "&path=" + record.path
        ).then(resp => {
            if (resp.data !== null) {
                // console.log(resp.data)
                const {roleNames, authorityNames} = resp.data.data
                setRoles([...roleNames])
                setAuthorities([...authorityNames])
            }
        }).catch(err => {
            console.log(err)
        })
        setIsModalOpen(true)
    }

    return (
        <>
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
                            title: 'Action',
                            dataIndex: 'action',
                            key: 'action',
                            render: (_, record) => (
                                <Button type="primary" onClick={() => requestResourceDetail(record)}>
                                    Detail
                                </Button>
                            )
                        }
                    ]}
                    dataSource={resources}
                />
            </Card>
            <Modal
                title="Resource Detail"
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

ViewResource.pageLayout = MainLayout

export default ViewResource