import MainLayout from "@/pages/main/layout";
import {Button, Card, Col, Form, Input, notification, Row, Select} from "antd";
import {useEffect, useState} from "react";
import axios from "axios";

function VerificationIndex() {

    const [notificationApi, contextHolder] = notification.useNotification()
    const [resourceOptions, setResourceOptions] = useState([])

    useEffect(() => {
        axios.get(
            "/api/resource/all"
        ).then(resp => {
            console.log(resp.data)
            if (resp.data !== null) {
                const {code, message, data: resources} = resp.data
                const r = []
                resources.forEach(resource => {
                    r.push({
                        key: resource.method + ":" + resource.path,
                        label: resource.path,
                        value: resource.path
                    })
                })
                setResourceOptions([...r])
            }
        }).catch(err => {
            console.log(err)
        })
    }, [])

    const verify = (values) => {
        const {username, method, path} = values
        axios.get(
            "/api/verification/verify?username=" + username + "&method=" + method + "&path=" + path
        ).then(resp => {
            if (resp.data !== null) {
                const {code, message, data: flag} = resp.data
                if (code === 200 && flag) {
                    notificationApi.success({
                        message: "user can visit"
                    })
                } else {
                    notificationApi.warning({
                        message: "user has insufficient authorities"
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
            <Card title="Verification Test">
                <Row>
                    <Col span={12}>
                        <Form
                            name="verificationForm"
                            labelCol={{
                                span: 8,
                            }}
                            wrapperCol={{
                                span: 16,
                            }}
                            onFinish={verify}
                            autoComplete="off"
                        >
                            <Form.Item
                                label="Request Method"
                                name="method"
                                rules={[
                                    {
                                        required: true,
                                        message: 'Please select request method!',
                                    },
                                ]}
                            >
                                <Select
                                    options={[
                                        {
                                            value: "POST",
                                            label: "POST"
                                        },
                                        {
                                            value: "DELETE",
                                            label: "DELETE"
                                        },
                                        {
                                            value: "PUT",
                                            label: "PUT"
                                        },
                                        {
                                            value: "PATCH",
                                            label: "PATCH"
                                        },
                                        {
                                            value: "GET",
                                            label: "GET"
                                        }
                                    ]}
                                />
                            </Form.Item>

                            <Form.Item
                                label="Path"
                                name="path"
                                rules={[
                                    {
                                        required: true,
                                        message: 'Please select path!',
                                    },
                                ]}
                            >
                                <Select
                                    options={resourceOptions}
                                />
                            </Form.Item>

                            <Form.Item
                                label="Username"
                                name="username"
                                rules={[
                                    {
                                        required: true,
                                        message: 'Please input username!',
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
                                <Button type="primary" htmlType="submit">
                                    Submit
                                </Button>
                            </Form.Item>
                        </Form>
                    </Col>
                    <Col span={12}></Col>
                </Row>
            </Card>
        </>
    )
}

VerificationIndex.pageLayout = MainLayout

export default VerificationIndex