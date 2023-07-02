import MainLayout from "@/pages/main/layout";
import {useEffect, useState} from "react";
import axios from "axios";
import {Button, Card, List, Modal, Popconfirm, Space, Table} from "antd";

function ViewAuthority() {
    const [loading, setLoading] = useState(false)
    const [authorities, setAuthorities] = useState([])

    const requestData = () => {
        setLoading(true)
        axios.get(
            "/api/authority/all"
        ).then(resp => {
            if (resp.data !== null) {
                const {data: dbAuthorities} = resp.data
                const displayData = []
                dbAuthorities.forEach(dbAuthority => {
                    displayData.push({
                        key: dbAuthority.id,
                        name: dbAuthority.name
                    })
                })
                // console.log(displayData)
                setAuthorities([...displayData])
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

    const confirmDeleteAuthority = (record) => {
        setLoading(true)
        axios.delete(
            "/api/authority/" + record.name
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
                                        title="Delete the authority"
                                        description="Are you sure to delete this authority?"
                                        onConfirm={() => confirmDeleteAuthority(record)}
                                        okText="Yes"
                                        cancelText="No"
                                    >
                                        <Button danger>Delete</Button>
                                    </Popconfirm>
                                </Space>
                            )
                        }
                    ]}
                    dataSource={authorities}
                    loading={loading}
                />
            </Card>
        </>
    )
}

ViewAuthority.pageLayout = MainLayout

export default ViewAuthority