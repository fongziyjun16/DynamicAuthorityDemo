import MainLayout from "@/pages/main/layout";
import {useState} from "react";
import {Button, Card, Col, Form, Input, notification, Row, Space} from "antd";
import {selectMethod} from "@/pages/main/components/common";
import axios from "axios";

function Verification() {

    const [notificationApi, contextHolder] = notification.useNotification();
    const [loading, setLoading] = useState(false)
    const [status, setStatus] = useState("")

    const verify = (values) => {
        setLoading(true)
        axios.get(
            "/api/verification/verify?username=" + values.username + "&method=" + values.method + "&path=" + values.path
        ).then(resp => {
            if (resp.data !== null) {
                const {code, message} = resp.data
                if (code === 200) {
                    notificationApi.success({
                        message: "Notification",
                        description: "Verification Pass. This user can have access to this resource"
                    })
                    setStatus("pass")
                } else {
                    notificationApi.error({
                        message: "Notification",
                        description: "Verification Fail"
                    })
                    setStatus("fail")
                }
                setLoading(false)
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
                            name="basic"
                            labelCol={{ span: 8 }}
                            wrapperCol={{ span: 16 }}
                            onFinish={verify}
                            autoComplete="off"
                        >
                            <Form.Item
                                label="Resource Method"
                                name="method"
                                rules={[{ required: true, message: 'Please input resource method!' }]}
                            >
                                {selectMethod}
                            </Form.Item>

                            <Form.Item
                                label="Resource Path"
                                name="path"
                                rules={[{ required: true, message: 'Please input recourse path!' }]}
                            >
                                <Input />
                            </Form.Item>

                            <Form.Item
                                label="Username"
                                name="username"
                                rules={[{ required: true, message: 'Please input username!' }]}
                            >
                                <Input/>
                            </Form.Item>

                            <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
                                <Space>
                                    <Button type="primary" htmlType="submit" disabled={loading}>
                                        Submit
                                    </Button>
                                    {
                                        status === "pass" ?
                                            <span style={{color: "green"}}>Verification Pass</span> :
                                            (
                                                status === "fail" ?
                                                    <span style={{color: "red"}}>Verification Fail</span> :
                                                    <span></span>
                                            )
                                    }
                                </Space>
                            </Form.Item>
                        </Form>
                    </Col>
                    <Col span={12}></Col>
                </Row>
            </Card>
        </>
    )
}

Verification.pageLayout = MainLayout

export default Verification