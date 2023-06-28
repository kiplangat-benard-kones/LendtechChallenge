USE [LENDTECH_DB]
GO

/****** Object:  Table [dbo].[TB_ELEVATOR_EVENTS]    Script Date: 28/06/2023 22:24:01 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[TB_ELEVATOR_EVENTS](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[event_type] [nvarchar](50) NULL,
	[description] [nvarchar](500) NULL,
	[timestamp] [timestamp] NULL,
 CONSTRAINT [PK_TB_ELEVATOR_EVENTS] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

CREATE PROCEDURE ASP_ELEVATOR_LOG_EVENTS 
	-- Add the parameters for the stored procedure here
	@event_type nvarchar(50), 
	@description nvarchar(500)
AS
BEGIN
BEGIN TRY
	BEGIN TRAN--TRANSACTION START
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	INSERT INTO [dbo].[TB_ELEVATOR_EVENTS]
           ([event_type]
           ,[description])
     VALUES
           (@event_type,
           @description)
		   COMMIT TRAN;
		   SELECT '00' AS ALERT_STATUS,'Event Logged successfully' AS Narration;
END TRY
	BEGIN CATCH
	    ROLLBACK TRANSACTION
		  SELECT '01' AS ALERT_STATUS, ERROR_MESSAGE() AS Narration;
		RETURN
	END CATCH
END
GO

CREATE TABLE [dbo].[TB_AUDIT_TRAIL](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[user_id] [nvarchar](50) NULL,
	[ip_address] [nvarchar](50) NULL,
	[end_point] [nvarchar](250) NULL,
	[query] [nvarchar](250) NULL,
	[timestamp] [timestamp] NULL,
 CONSTRAINT [PK_TB_AUDIT_TRAIL] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[TB_ELEVATOR]    Script Date: 28/06/2023 22:25:43 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TB_ELEVATOR](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[current_floor] [nvarchar](50) NULL,
	[state] [nvarchar](50) NULL,
	[direction] [nvarchar](50) NULL,
	[created_at] [datetime] NULL,
 CONSTRAINT [PK_TB_ELEVATOR] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[TB_FLOOR]    Script Date: 28/06/2023 22:25:43 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TB_FLOOR](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_TB_FLOOR] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[TB_LOG]    Script Date: 28/06/2023 22:25:43 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TB_LOG](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[elevator_id] [int] NULL,
	[event_type] [nvarchar](50) NULL,
	[description] [nvarchar](50) NULL,
	[timestamp] [timestamp] NULL,
 CONSTRAINT [PK_TB_LOG] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[TB_ELEVATOR] ADD  CONSTRAINT [DF_TB_ELEVATOR_created_at]  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[TB_LOG]  WITH CHECK ADD  CONSTRAINT [FK_TB_LOG_TB_ELEVATOR] FOREIGN KEY([elevator_id])
REFERENCES [dbo].[TB_ELEVATOR] ([id])
GO
ALTER TABLE [dbo].[TB_LOG] CHECK CONSTRAINT [FK_TB_LOG_TB_ELEVATOR]
GO

